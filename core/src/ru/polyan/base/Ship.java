package ru.polyan.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.polyan.math.Rect;
import ru.polyan.pool.BulletPool;
import ru.polyan.pool.ExplosionPool;
import ru.polyan.sprite.Bullet;
import ru.polyan.sprite.Explosion;
import ru.polyan.utils.EnemyEmitter;

public class Ship extends Sprite{

    protected float shootInterval;
    protected Vector2 shipVelositi;

    protected Rect worldBounds;

    protected boolean dualShoot = false;
    protected int currentGun = 0;

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

    protected boolean flagman = false;
    protected float launchPeriod = 0f;

    public boolean isFlagman() {
        return flagman;
    }

    public float getLaunchPeriod() {
        return launchPeriod;
    }

    public void setLaunchPeriod(float launchPeriod) {
        this.launchPeriod = launchPeriod;
    }

    public void addLaunchPeriod(float launchPeriod) {
        this.launchPeriod += launchPeriod;
    }

    protected int hp;

    protected ExplosionPool explosionPool;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        weaponIsReady = false;
        hitPeriod = 0.5f;
    }

    public int getBulletDamage() {
        return bulletDamage;
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
                    explode();
                }
            }else{
                hitTimer -= delta;
            }
        }

    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x + getHalfWidth() * currentGun*0.5f, bulletPos.y);
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, bulletDamage);
        shootSound.play(0.1f);
        currentGun = currentGun*(-1);
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

    public void hit() {
        isHit = true;
        frame = 1;
        hitTimer = hitPeriod;
    }

    public void explode(){
        Explosion e = explosionPool.obtain();
        e.set(pos, getHeight());
    }

    public void launch(EnemyEmitter enemyEmitter) {
    }
}
