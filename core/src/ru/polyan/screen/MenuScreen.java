package ru.polyan.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    Texture img;

    private Vector2 currentPos;
    private Vector2 newPos;

    private Vector2 currentVelocity;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        currentPos = new Vector2();
        newPos = new Vector2();
        currentVelocity = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!newPos.epsilonEquals(currentPos)){

            currentVelocity.x = newPos.x - currentPos.x;
            currentVelocity.y = newPos.y - currentPos.y;
            currentVelocity.nor();
            if(newPos.dst(currentPos)>10f){
                currentVelocity.scl(5);
            }

            currentPos.add(currentVelocity);

        }else{
            currentPos.x = newPos.x;
            currentPos.y = newPos.y;
            currentVelocity.x = 0;
            currentVelocity.y = 0;
        }

        batch.begin();
        batch.draw(img, currentPos.x, currentPos.y);
        batch.end();

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        newPos.set(screenX, Gdx.graphics.getHeight() - screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

}
