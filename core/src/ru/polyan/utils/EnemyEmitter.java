package ru.polyan.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.math.Rect;
import ru.polyan.pool.EnemyPool;
import ru.polyan.sprite.EnemyShip;

public class EnemyEmitter {

    private static final float GENERATE_INTERVAL = 4f;

    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_SMALL_HP = 1;
    private static final boolean ENEMY_SMALL_DUALSHOOT = false;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.15f;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.02f;
    private static final int ENEMY_MEDIUM_BULLET_DAMAGE = 5;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_MEDIUM_HP = 5;
    private static final boolean ENEMY_MEDIUM_DUALSHOOT = false;

    private static final float MINIMUM_RELOAD_INTERVAL = 0.5f;


    private static final float ENEMY_BIG_HEIGHT = 0.2f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static final int ENEMY_BIG_BULLET_DAMAGE = 10;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 1f;
    private static final int ENEMY_BIG_HP = 10;
    private static final boolean ENEMY_BIG_DUALSHOOT = true;

    private final static Vector2 ENEMY_SMAL_BULLET_V = new Vector2(0, -0.3f);
    private final static Vector2 ENEMY_MEDIUM_BULLET_V = new Vector2(0, -0.25f);
    private final static Vector2 ENEMY_BIG_BULLET_V = new Vector2(0, -0.3f);
    private final static Vector2 ENEMY_SMALL_V = new Vector2(0, -0.2f);
    private final static Vector2 ENEMY_MEDIUM_V = new Vector2(0, -0.03f);
    private final static Vector2 ENEMY_BIG_V = new Vector2(0, -0.005f);

    private final Vector2 enemySmallBulletV = new Vector2(ENEMY_SMAL_BULLET_V);
    private final Vector2 enemyMediumBulletV = new Vector2(ENEMY_MEDIUM_BULLET_V);
    private final Vector2 enemyBigBulletV = new Vector2(ENEMY_BIG_BULLET_V);
    private final Vector2 enemySmallV = new Vector2(ENEMY_SMALL_V);
    private final Vector2 enemyMediumV = new Vector2(ENEMY_MEDIUM_V);
    private final Vector2 enemyBigV = new Vector2(ENEMY_BIG_V);

    private float enemySmallReloadInterval = ENEMY_SMALL_RELOAD_INTERVAL;
    private float enemyMediumReloadInterval = ENEMY_MEDIUM_RELOAD_INTERVAL;
    private float enemyBigReloadInterval = ENEMY_BIG_RELOAD_INTERVAL;



    private final Rect worldBounds;
    private final Sound bulletSound;

    private float generateTimer;

    private final TextureRegion[] enemySmallRegions;
    private final TextureRegion[] enemyMediumRegions;
    private final TextureRegion[] enemyBigRegions;
    private final TextureRegion bulletRegion;

    private final EnemyPool enemyPool;

    private int level;

    public EnemyEmitter(TextureAtlas atlas, EnemyPool enemyPool, Rect worldBounds, Sound bulletSound) {
        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;
        this.bulletSound = bulletSound;
        enemySmallRegions = Regions.split(atlas.findRegion("enemy0"), 1, 2, 2);
        enemyMediumRegions = Regions.split(atlas.findRegion("enemy1"), 1, 2, 2);
        enemyBigRegions = Regions.split(atlas.findRegion("enemy2"), 1, 2, 2);
        bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta, int frags) {
        level = frags / 10 +1;
        generateTimer += delta;
        float generatedInterval = GENERATE_INTERVAL - ((float)level/10);
        generatedInterval = generatedInterval>0.7f ? generatedInterval : generatedInterval;
        if (generateTimer >= generatedInterval) {
            generateTimer = 0f;
            EnemyShip enemyShip = enemyPool.obtain();
            float type = (float) Math.random();

            enemySmallBulletV.set(ENEMY_SMAL_BULLET_V);
            enemyMediumBulletV.set(ENEMY_MEDIUM_BULLET_V);
            enemyBigBulletV.set(ENEMY_BIG_BULLET_V);
            enemySmallV.set(ENEMY_SMALL_V);
            enemyMediumV.set(ENEMY_MEDIUM_V);
            enemyBigV.set(ENEMY_BIG_V);

            enemyMediumBulletV.mulAdd(ENEMY_MEDIUM_BULLET_V, (float)level/15);
            enemyBigBulletV.mulAdd(ENEMY_BIG_BULLET_V, (float)level/10);
            enemySmallBulletV.mulAdd(ENEMY_SMAL_BULLET_V, (float)level/15);

            enemySmallReloadInterval = ENEMY_SMALL_RELOAD_INTERVAL - ((float)level/3);
            enemyMediumReloadInterval = ENEMY_MEDIUM_RELOAD_INTERVAL  - ((float)level/5);
            enemyBigReloadInterval = ENEMY_BIG_RELOAD_INTERVAL  - ((float)level/10);

            enemySmallReloadInterval = enemySmallReloadInterval>MINIMUM_RELOAD_INTERVAL ? enemySmallReloadInterval : MINIMUM_RELOAD_INTERVAL;
            enemyMediumReloadInterval = enemyMediumReloadInterval>MINIMUM_RELOAD_INTERVAL ? enemyMediumReloadInterval : MINIMUM_RELOAD_INTERVAL;
            enemyBigReloadInterval = enemyBigReloadInterval>MINIMUM_RELOAD_INTERVAL ? enemyBigReloadInterval : MINIMUM_RELOAD_INTERVAL;

            enemyMediumV.mulAdd(ENEMY_MEDIUM_V, (float)level/20);
            enemyBigV.mulAdd(ENEMY_BIG_V, (float)level/20);

            if ((level<2 || type < 0.5f) || (level<6 && type < 0.3f)) {
                enemyShip.set(
                        enemySmallRegions,
                        enemySmallV,
                        bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        enemySmallBulletV,
                        ENEMY_SMALL_BULLET_DAMAGE,
                        enemySmallReloadInterval,
                        bulletSound,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP,
                        ENEMY_SMALL_DUALSHOOT,
                        level > 2,
                        false
                );

            } else if (
                    (level<3 && type < 0.8f)
                    || (level<5 && type < 0.7f)
                    || (level<10 && type < 0.6f)
                ) {
                enemyShip.set(
                        enemyMediumRegions,
                        enemyMediumV,
                        bulletRegion,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        enemyMediumBulletV,
                        ENEMY_MEDIUM_BULLET_DAMAGE,
                        enemyMediumReloadInterval,
                        bulletSound,
                        ENEMY_MEDIUM_HEIGHT,
                        ENEMY_MEDIUM_HP,
                        ENEMY_MEDIUM_DUALSHOOT,
                        level > 3,
                        false
                );
            } else {
                enemyShip.set(
                        enemyBigRegions,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        enemyBigBulletV,
                        ENEMY_BIG_BULLET_DAMAGE,
                        enemyBigReloadInterval,
                        bulletSound,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP,
                        ENEMY_BIG_DUALSHOOT,
                        level > 4,
                        level > 7
                );
            }

            enemyShip.pos.x = MathUtils.random(
                    worldBounds.getLeft() + enemyShip.getHalfWidth(),
                    worldBounds.getRight() - enemyShip.getHalfWidth()
            );

            enemyShip.setBottom(worldBounds.getTop());
        }
    }

    public void launchSmalShip(Vector2 pos) {

        enemySmallBulletV.set(ENEMY_SMAL_BULLET_V);
        enemySmallV.set(ENEMY_SMALL_V);

        enemySmallBulletV.mulAdd(ENEMY_SMAL_BULLET_V, (float)level/15);

        enemySmallReloadInterval = ENEMY_SMALL_RELOAD_INTERVAL - ((float)level/3);

        enemySmallReloadInterval = enemySmallReloadInterval>MINIMUM_RELOAD_INTERVAL ? enemySmallReloadInterval : MINIMUM_RELOAD_INTERVAL;
        EnemyShip enemyShip = enemyPool.obtain();
        enemyShip.set(
                    enemySmallRegions,
                    enemySmallV,
                    bulletRegion,
                    ENEMY_SMALL_BULLET_HEIGHT,
                    enemySmallBulletV,
                    ENEMY_SMALL_BULLET_DAMAGE + (level/5),
                    enemySmallReloadInterval,
                    bulletSound,
                    ENEMY_SMALL_HEIGHT,
                    ENEMY_SMALL_HP,
                    ENEMY_SMALL_DUALSHOOT,
                    level > 2,
                    false
            );
        enemyShip.pos.set(pos);
    }


    public int getLevel() {
        return level;
    }
}
