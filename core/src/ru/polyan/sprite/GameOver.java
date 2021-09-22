package ru.polyan.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.polyan.base.Sprite;
import ru.polyan.math.Rect;

public class GameOver extends Sprite {

    private final static float HEIGHT = 0.07f;

    public GameOver(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setBottom(0.2f);
    }

}
