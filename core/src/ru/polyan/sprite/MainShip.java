package ru.polyan.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.base.Sprite;
import ru.polyan.math.Rect;
import ru.polyan.pool.BulletPool;

public class MainShip extends Sprite {

    private static final float BOTTOM_POS = 0.05f;
    private static final Vector2 SHIP_VELOSITI = new Vector2(0.005f,0);
    private static final float SHOT_INTERVAL = 0.5f;

    private Rect worldBounds;

    private TextureRegion shipState[][];

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isSpaceKeyPressed;

    private float halfField;

    private Vector2 currentVel;

    private final BulletPool bulletPool;
    private final TextureRegion bulletRegion;
    private final Vector2 bulletV;
    private final Vector2 bulletPos;
    private final float bulletHeight;
    private final int bulletDamage;

    private float shootTimer;

    private Sound shootSound;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"));
        shipState = regions[0].split(regions[0].getRegionWidth()/2, regions[0].getRegionHeight());
        regions[0] = shipState[0][0];
        currentVel = new Vector2(0,0);
        pos.set(0,0);
        isLeftKeyPressed = false;
        isRightKeyPressed = false;

        this.bulletPool = bulletPool;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        bulletHeight = 0.01f;
        bulletDamage = 1;

        shootTimer = 0;

        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));

    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        halfField = 0;
        setHeight(0.1f);
        setHeightProportion(getHeight());
        setBottom(worldBounds.getBottom() + BOTTOM_POS);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        currentVel.set(SHIP_VELOSITI);
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
        shootTimer+=delta;
        if(shootTimer>SHOT_INTERVAL){
            shootTimer=0;
            shoot();
        }
        processKeyEvent();
        if(!currentVel.isZero()) {
            pos.add(currentVel);
        }
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
            currentVel.set(SHIP_VELOSITI);
            currentVel.x = -currentVel.x;
        }else if(isRightKeyPressed){
            currentVel.set(SHIP_VELOSITI);
        }
        else{
            currentVel.setZero();
        }

        if(isSpaceKeyPressed){
            shoot();
            isSpaceKeyPressed = false;
        }

    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, bulletDamage);
        shootSound.play(1f);
    }

    public void dispose(){
        shootSound.dispose();
    }

}
