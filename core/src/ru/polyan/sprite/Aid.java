package ru.polyan.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.polyan.base.Sprite;
import ru.polyan.math.Rect;
import ru.polyan.pool.BulletPool;
import ru.polyan.pool.ExplosionPool;
import ru.polyan.sprite.Bullet;
import ru.polyan.sprite.Explosion;
import ru.polyan.utils.EnemyEmitter;

public class Aid extends Sprite {

    protected Rect worldBounds;

    protected Vector2 v;

    protected int health;

    public Aid(Rect worldBounds) {
        regions = new TextureRegion[1];
        regions[0] = new TextureRegion();
        this.health = 0;
        this.worldBounds = worldBounds;
    }

    public void set(TextureRegion[] region, int health, Vector2 velocity) {
        this.regions = region;
        this.health = health;
        setHeightProportion(0.05f);
        this.v = velocity;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

}
