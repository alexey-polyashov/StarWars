package ru.polyan.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.base.Sprite;
import ru.polyan.math.Rect;

public class MainShip extends Sprite {

    private static final float BOTTOM_POS = 0.05f;
    private static final Vector2 SHIP_VELOSITI = new Vector2(0.005f,0);

    private Rect worldBounds;

    private TextureRegion shipState[][];

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;

    private float halfField;

    private Vector2 currentVel;
    private Vector2 currentPos;

    public MainShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        shipState = regions[0].split(regions[0].getRegionWidth()/2, regions[0].getRegionHeight());
        regions[0] = shipState[0][0];
        currentVel = new Vector2(0,0);
        currentPos = new Vector2(0,0);
        isLeftKeyPressed = false;
        isRightKeyPressed = false;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        halfField = 0;
        setHeight(0.1f);
        setHeightProportion(getHeight());
        setBottom(worldBounds.getBottom() + BOTTOM_POS);
        setLeft(currentPos.x);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        currentVel.set(SHIP_VELOSITI);
        if(touch.x < halfField){
            currentVel.x = -currentVel.x;
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        currentVel.setZero();
        return false;
    }

    @Override
    public void update(float delta) {
        if(!currentVel.isZero()) {
            checkAndHandleBounds();
            currentPos.add(currentVel);
            System.out.println(currentPos);
            setLeft(currentPos.x);
        }
    }

    private void checkAndHandleBounds() {
        if (currentPos.x < worldBounds.getLeft()) {
            currentPos.x = worldBounds.getLeft();
            currentVel.setZero();
        }else if(currentPos.x > (worldBounds.getRight() - getWidth())){
            currentPos.x = worldBounds.getRight() - getWidth();
            currentVel.setZero();
        }
    }

    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT){
            isLeftKeyPressed = false;
        }else if(keycode == Input.Keys.RIGHT){
            isRightKeyPressed = false;
        }
        processKeyEvent();
        return false;
    }

    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT){
            isLeftKeyPressed = true;
        }else if(keycode == Input.Keys.RIGHT){
            isRightKeyPressed = true;
        }
        processKeyEvent();
        return false;
    }

    private void processKeyEvent(){
        if(isLeftKeyPressed){
            currentVel.set(SHIP_VELOSITI);
            currentVel.x = -currentVel.x;
        }else if(isRightKeyPressed){
            currentVel.set(SHIP_VELOSITI);
        }else{
            currentVel.setZero();
        }
    }

}
