package ru.polyan.sprite;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.base.Ship;
import ru.polyan.math.Rect;
import ru.polyan.pool.BulletPool;
import ru.polyan.pool.ExplosionPool;

public class EnemyShip extends Ship {

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
            currentVel.set(shipVelositi);
        }else{
            currentVel.set(0, -0.5f);
        }
        bulletPos.set(pos.x, pos.y - getHalfHeight());
        if(getTop()<worldBounds.getBottom()){
            destroy();
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
            int hp
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
        shootTimer = 0;
        this.bulletPos.set(pos.x, pos.y - getHalfHeight());
        currentVel.set(shipVelositi);
    }

}
