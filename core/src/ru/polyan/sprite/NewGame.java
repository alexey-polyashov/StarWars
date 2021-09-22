package ru.polyan.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.base.Sprite;
import ru.polyan.math.Rect;
import ru.polyan.screen.GameScreen;

public class NewGame extends Sprite {

    private final static float HEIGHT = 0.05f;
    private final GameScreen game;

    boolean touchOn;


    public NewGame(TextureAtlas atlas, GameScreen game) {
        super(atlas.findRegion("button_new_game"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setBottom(0.0f);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if(isMe(touch)){
            touchOn = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if(touchOn && isMe(touch)){
            game.newGame();
        }
        touchOn = false;
        return false;
    }
}
