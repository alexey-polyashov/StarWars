package ru.polyan.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.polyan.base.Ship;
import ru.polyan.math.Rect;
import ru.polyan.pool.BulletPool;

public class MainShip extends Ship {

    private static final float BOTTOM_POS = 0.05f;
    private static final float SHOT_INTERVAL = 0.5f;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isSpaceKeyPressed;

    private float halfField;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool) {

        super(atlas.findRegion("main_ship"), 1,2,2);

        weaponIsReady = true;
        shipVelositi = new Vector2(0.3f,0);
        currentVel = new Vector2(0,0);
        pos.set(0,0);
        isLeftKeyPressed = false;
        isRightKeyPressed = false;

        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletV = new Vector2(0, 0.5f);
        this.bulletPos = new Vector2();
        this.bulletHeight = 0.01f;
        this.bulletDamage = 1;

        this.shootTimer = 0;
        this.shootInterval = SHOT_INTERVAL;

        this.shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));

    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        halfField = worldBounds.pos.x;
        setHeight(0.1f);
        setHeightProportion(getHeight());
        setBottom(worldBounds.getBottom() + BOTTOM_POS);
        bulletPos.set(pos.x, pos.y + getHalfHeight());
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        currentVel.set(shipVelositi);
        if(touch.x < halfField){
            isLeftKeyPressed = true;
            isRightKeyPressed = false;
            currentVel.x = -currentVel.x;
        }else{
            isLeftKeyPressed = false;
            isRightKeyPressed = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        currentVel.setZero();
        if(touch.x < halfField){
            isLeftKeyPressed = false;
            currentVel.x = -currentVel.x;
        }else{
            isRightKeyPressed = false;
        }
        return false;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        processKeyEvent();
        bulletPos.set(pos.x, pos.y + getHalfHeight());
    }

    private void checkAndHandleBounds() {
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            currentVel.setZero();
        }else if(getRight() > worldBounds.getRight()){
            setRight(worldBounds.getRight());
            currentVel.setZero();
        }
    }

    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT){
            isLeftKeyPressed = false;
        }else if(keycode == Input.Keys.RIGHT){
            isRightKeyPressed = false;
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT){
            isLeftKeyPressed = true;
        }else if(keycode == Input.Keys.RIGHT){
            isRightKeyPressed = true;
        }else if(keycode == Input.Keys.SPACE){
            isSpaceKeyPressed = true;
        }
        return false;
    }

    private void processKeyEvent(){
        checkAndHandleBounds();
        if(isLeftKeyPressed){
            currentVel.set(shipVelositi);
            currentVel.x = -currentVel.x;
        }else if(isRightKeyPressed){
            currentVel.set(shipVelositi);
        }
        else{
            currentVel.setZero();
        }

        if(isSpaceKeyPressed){
            shoot();
            isSpaceKeyPressed = false;
        }

    }


}
