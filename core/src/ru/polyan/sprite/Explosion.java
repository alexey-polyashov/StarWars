package ru.polyan.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.base.Sprite;

public class Explosion extends Sprite {

    private final static float ANIMATE_INTERVAL = 0.015f;

    private float animateTimer;
    private Sound expSound;

    public Explosion(TextureAtlas atlas, Sound expSound) {
        super(atlas.findRegion("explosion"), 9,9,74);
        this.expSound = expSound;
    }

    public void set(Vector2 pos, float height){
        this.pos.set(pos);
        setHeightProportion(height);
        expSound.play(0.1f);
    }

    public void update(float delta){
        animateTimer += delta;
        if(animateTimer >= ANIMATE_INTERVAL){
            animateTimer = 0;
            if(++frame == regions.length){
                destroy();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }
}
