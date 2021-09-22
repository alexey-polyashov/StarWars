package ru.polyan.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.polyan.base.SpritesPool;
import ru.polyan.sprite.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {

    private final TextureAtlas atlas;
    private final Sound expSound;

    public ExplosionPool(TextureAtlas atlas, Sound expSound) {
        this.atlas = atlas;
        this.expSound = expSound;
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(atlas, expSound);
    }
}
