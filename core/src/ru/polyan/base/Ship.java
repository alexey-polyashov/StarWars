package ru.polyan.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.math.Rect;
import ru.polyan.pool.BulletPool;
import ru.polyan.sprite.Bullet;

public class Ship extends Sprite{

    protected float shootInterval;
    protected Vector2 shipVelositi;

    protected Rect worldBounds;

    protected Vector2 currentVel;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;
    protected float bulletHeight;
    protected int bulletDamage;

    protected float shootTimer;
    protected Sound shootSound;

    protected int hp;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {

        shootTimer+=delta;
        if(shootTimer>shootInterval){
            shootTimer=0;
            shoot();
        }
        if(!currentVel.isZero()) {
            pos.mulAdd(currentVel, delta);
        }
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, bulletDamage);
        shootSound.play();
    }

    public void dispose(){
        shootSound.dispose();
    }
}
