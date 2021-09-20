package ru.polyan.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.polyan.math.Rect;
import ru.polyan.pool.BulletPool;
import ru.polyan.sprite.Bullet;

public class Ship extends Sprite{

    protected float shootInterval;
    protected Vector2 shipVelositi;

    protected Rect worldBounds;

    protected boolean weaponIsReady;
    protected boolean isHit;
    protected float hitTimer;
    protected float hitPeriod;

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

    protected Bullet heatBullet;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        weaponIsReady = false;
        hitPeriod = 0.5f;
        heatBullet = new Bullet();
    }

    @Override
    public void update(float delta) {

        shootTimer+=delta;
        if(weaponIsReady && shootTimer>shootInterval){
            shootTimer=0;
            shoot();
        }
        if(!currentVel.isZero()) {
            pos.mulAdd(currentVel, delta);
        }
        if(isHit){
            if(hitTimer<0){
                frame = 0;
                hitTimer = 0;
                isHit = false;
                if(hp<=0){
                    destroy();
                }
            }else{
                hitTimer -= delta;
            }
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

    public List<Bullet> getActiveBullets(){
        return bulletPool.getActiveObjects();
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Bullet getHeatBullet() {
        return heatBullet;
    }

    public void hit(Bullet bullet) {
        if(heatBullet != bullet) {
            heatBullet = bullet;
            isHit = true;
            if (isHit) {
                frame = 1;
                hitTimer = hitPeriod;
            }
        }
    }
}
