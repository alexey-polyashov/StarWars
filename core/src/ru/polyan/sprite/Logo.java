package ru.polyan.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.base.Sprite;
import ru.polyan.math.Rect;

public class Logo extends Sprite {

    private Vector2 currentPos;
    private Vector2 newPos;

    private Vector2 currentVelocity;

    public Logo(Texture texture) {
        super(new TextureRegion(texture));

        setScale(0.3f);

        currentPos = new Vector2();
        newPos = new Vector2();
        currentVelocity = new Vector2();

    }

    @Override
    public void resize(Rect worldBounds) {
        this.pos.set(worldBounds.pos);
        setHeightProportion(worldBounds.getHeight());
    }

    public void setNewPosition(Vector2 newPosition){
        newPos.set(newPosition);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        float distance = newPos.dst(currentPos);

        if (!(distance<0.01f)){

            currentVelocity.x = newPos.x - currentPos.x;
            currentVelocity.y = newPos.y - currentPos.y;
            currentVelocity.nor().scl(0.01f);
            if(newPos.dst(currentPos)>0.1f){
                currentVelocity.scl(5);
            }

            currentPos.add(currentVelocity);

        }else{
            currentPos.x = newPos.x;
            currentPos.y = newPos.y;
            currentVelocity.setZero();
        }

        setLeft(currentPos.x);
        setBottom(currentPos.y);

    }
}
