package ru.polyan.pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.math.Rect;
import ru.polyan.sprite.Aid;
import ru.polyan.base.SpritesPool;

public class AidsPool extends SpritesPool<Aid> {

    private final static Vector2 VELOCITY = new Vector2(0, -0.1f);
    private final TextureRegion[] regions;
    private Rect worldBounds;

    @Override
    protected Aid newObject() {
        return new Aid(worldBounds);
    }

    public AidsPool(String filename, Rect worldBounds) {
        Texture texture = new Texture(Gdx.files.internal(filename));
        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        regions = new TextureRegion[1];
        regions[0] = region;
        this.worldBounds = worldBounds;
    }

    public Aid obtain() {
        Aid object;
        if (freeObjects.isEmpty()) {
            object = newObject();
            object.set(regions, 20, VELOCITY);
        } else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }
        activeObjects.add(object);
        //System.out.println(getClass().getSimpleName() + " active/free : " + activeObjects.size() + " / " + freeObjects.size());
        return object;
    }
}
