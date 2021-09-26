package ru.polyan.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import ru.polyan.base.BaseScreen;
import ru.polyan.base.Font;
import ru.polyan.base.Ship;
import ru.polyan.math.Rect;
import ru.polyan.pool.AidsPool;
import ru.polyan.pool.BulletPool;
import ru.polyan.pool.EnemyPool;
import ru.polyan.pool.ExplosionPool;
import ru.polyan.sprite.Aid;
import ru.polyan.sprite.Background;
import ru.polyan.sprite.Bullet;
import ru.polyan.sprite.EnemyShip;
import ru.polyan.sprite.GameOver;
import ru.polyan.sprite.MainShip;
import ru.polyan.sprite.NewGame;
import ru.polyan.sprite.Star;
import ru.polyan.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;
    private static final float FONT_MARGIN = 0.01f;

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";


    private Texture bg;
    private TextureAtlas atlas;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;
    private int fragsCount;


    private Background background;
    private Star[] stars;
    private MainShip mainShip;

    private BulletPool bulletPool;
    private AidsPool aidsPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private EnemyEmitter enemyEmitter;

    Music bgMusic;
    private Sound bulletSound;
    private Sound explosoinSound;

    private GameOver gameOverSprite;
    private NewGame newGameSprite;

    boolean gameOver;

    @Override
    public void show() {

        super.show();
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas("textures/mainAtlas.tpack");

        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosoinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        background = new Background(bg);
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosoinSound);
        mainShip = new MainShip(atlas, bulletPool, explosionPool);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, bulletSound);
        aidsPool = new AidsPool("aid.png", worldBounds);

        gameOverSprite = new GameOver(atlas);
        newGameSprite = new NewGame(atlas, this);

        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }

        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        bgMusic.setLooping(true);
        bgMusic.play();

        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(0.02f);

        fragsCount = 0;

        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        gameOverSprite.resize(worldBounds);
        newGameSprite.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        aidsPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        bgMusic.dispose();
        bulletSound.dispose();
        explosoinSound.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        super.touchDown(touch, pointer, button);
        if(!mainShip.isDestroyed()) {
            mainShip.touchDown(touch, pointer, button);
        }else{
            newGameSprite.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        super.touchUp(touch, pointer, button);
        if(!mainShip.isDestroyed()) {
            mainShip.touchUp(touch, pointer, button);
        }else{
            newGameSprite.touchUp(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        super.keyDown(keycode);
        if(!mainShip.isDestroyed()) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        super.keyUp(keycode);
        if(!mainShip.isDestroyed()) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    private void update(float delta) {

        for (Star star : stars) {
            star.update(delta);
        }

        if(!mainShip.isDestroyed()) {
            mainShip.update(delta);
        }else if(!gameOver){
            gameOver = true;
            enemyPool.reset();
        }

        if(!gameOver) {
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, fragsCount);
            aidsPool.updateActiveSprites(delta);
            checkDamage();
            flagmanAction(delta);
        }

        explosionPool.updateActiveSprites(delta);

    }

    private void flagmanAction(float delta) {
        for(int i=0; i<enemyPool.getActiveObjects().size(); i++) {
            Ship e = enemyPool.getActiveObjects().get(i);
            if(e.isFlagman()){
                if(e.getLaunchPeriod() > 0){
                    e.addLaunchPeriod(-delta);
                }else{
                    e.setLaunchPeriod((float) Math.random() * 10f);
                    e.launch(enemyEmitter);
                }
            }
        }
    }

    private void checkDamage(){

        for(Bullet b: mainShip.getActiveBullets()){

            if(b.isDestroyed()){
                continue;
            }

            if(b.getOwner()!=mainShip){
                if(!mainShip.isDestroyed()) {
                    if (isCollision(mainShip, b, b.getHalfWidth()*0.5f) && b.getTop() < (mainShip.pos.y)) {
                        mainShip.setHp(mainShip.getHp() - b.getDamage());
                        mainShip.hit();
                        b.destroy();
                    }
                }
            }else{
                for(EnemyShip e: enemyPool.getActiveObjects()){
                    if (isCollision(e, b, b.getHalfWidth()) && b.getTop() > (e.pos.y)) {
                        e.setHp(e.getHp()-b.getDamage());
                        e.hit();
                        b.destroy();
                        if(e.getHp()<=0){
                            fragsCount++;
                            float aidChance = (float)Math.random();
                            if(e.isFlagman() && aidChance > 0.5f) {
                                Aid aid = aidsPool.obtain();
                                aid.pos.set(e.pos);
                            }
                        }
                    }
                }
            }

        }

        if(!mainShip.isDestroyed()) {
            for (EnemyShip e : enemyPool.getActiveObjects()) {
                float shipDst = e.pos.dst(mainShip.pos);
                if (shipDst <= (e.getHalfWidth() + mainShip.getHalfWidth())) {
                    e.setHp(0);
                    e.destroy();
                    e.explode();
                    mainShip.hit();
                    mainShip.setHp(mainShip.getHp() - e.getBulletDamage());
                }
            }
            for (Aid a : aidsPool.getActiveObjects()) {
                float shipDst = a.pos.dst(mainShip.pos);
                if (shipDst <= (a.getHalfWidth() + mainShip.getHalfWidth())) {
                    a.destroy();
                    mainShip.setHp(mainShip.getHp() + a.getHealth());
                }
            }
        }

    }

    private boolean isCollision(Rect rec1, Rect rec2, float allow){

        if(
                rec1.getLeft()<(rec2.getRight()-allow) && rec1.getRight()>(rec2.getLeft()+allow)
                &&
                rec1.getTop()>(rec2.getBottom()+allow) && rec1.getBottom()<(rec2.getTop()-allow)
        ){
            return true;
        }
        return false;

    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        aidsPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        if(!mainShip.isDestroyed()) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            aidsPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        }
        explosionPool.drawActiveSprites(batch);
        if(gameOver){
            gameOverSprite.draw(batch);
            newGameSprite.draw(batch);
        }
        printInfo();
        batch.end();
    }

    public void newGame(){
        bulletPool.reset();
        aidsPool.reset();
        enemyPool.reset();
        explosionPool.reset();
        mainShip.reset();
        gameOver = false;
        fragsCount = 0;
    }

    private void printInfo(){
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(fragsCount),worldBounds.getLeft() + FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN);
        sbHp.setLength(0);
        font.draw(batch, sbHp.append(HP).append(mainShip.getHp()),worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()),worldBounds.getRight() - FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN, Align.right);
    }

}
