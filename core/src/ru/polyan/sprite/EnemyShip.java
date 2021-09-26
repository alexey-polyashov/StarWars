package ru.polyan.sprite;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.base.Ship;
import ru.polyan.math.Rect;
import ru.polyan.pool.BulletPool;
import ru.polyan.pool.ExplosionPool;
import ru.polyan.utils.EnemyEmitter;

public class EnemyShip extends Ship {

    protected boolean xMov;
    protected float moveChangePeriod = 0f;

    public EnemyShip(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.bulletV = new Vector2();
        this.bulletPos = new Vector2();
        this.shipVelositi = new Vector2();
        this.currentVel = new Vector2();
        this.explosionPool = explosionPool;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(getTop() < worldBounds.getTop()){
            if(!weaponIsReady) {
                shootTimer = shootInterval*0.8f;
                weaponIsReady = true;
            }
            currentVel.y = shipVelositi.y;
        }else{
            currentVel.set(0, -0.5f);
        }
        if(xMov){
            if(moveChangePeriod > 0){
                moveChangePeriod-=delta;
            }else{
                moveChangePeriod = (float) Math.random() * 2f;
                currentVel.x = ((float) Math.random()-0.5f)*0.3f ;
            }
            //System.out.println(moveChangePeriod + " " + currentVel.x);
        }
        bulletPos.y = pos.y - getHalfHeight();
        if(dualShoot && xMov){
            bulletPos.x = pos.x + getHalfWidth() * currentGun*0.5f;
        }
        checkBounds();
    }

    public void launch(EnemyEmitter enemyEmitter) {
        enemyEmitter.launchSmalShip(pos);
    }

    private void checkBounds(){
        if(getTop()<worldBounds.getBottom()){
            destroy();
        }
        if(getRight()<worldBounds.getRight()){
            currentVel.x = -currentVel.x;
        }
        if(getLeft()>worldBounds.getLeft()){
            currentVel.x = -currentVel.x;
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 shipVelositi,
            TextureRegion bulletRegion,
            float bulletHeight,
            Vector2 bulletV,
            int bulletDamage,
            float reloadInterval,
            Sound bulletSound,
            float height,
            int hp,
            boolean dualShoot,
            boolean xMov,
            boolean flagman
    ) {

        this.regions = regions;
        this.shipVelositi.set(shipVelositi);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(bulletV);
        this.bulletDamage = bulletDamage;
        this.shootInterval = reloadInterval;
        this.shootSound = bulletSound;
        setHeightProportion(height);
        this.hp = hp;
        this.flagman = flagman;
        this.dualShoot = dualShoot;
        this.xMov = xMov;

        launchPeriod = (float) Math.random() * 3f;
        shootTimer = 0;
        if(dualShoot){
            currentGun = 1;
        }else{
            currentGun = 0;
        }

        this.bulletPos.set(pos.x + getHalfWidth() * currentGun, pos.y - getHalfHeight());
        currentVel.set(shipVelositi);

    }

}
