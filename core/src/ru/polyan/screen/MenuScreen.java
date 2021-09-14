package ru.polyan.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.polyan.base.BaseScreen;
import ru.polyan.math.Rect;
import ru.polyan.sprite.Background;
import ru.polyan.sprite.Logo;

public class MenuScreen extends BaseScreen {

    private Background background;
    private Logo logo;

    private Texture img;
    private Texture bg;

    @Override
    public void show() {
        super.show();

        img = new Texture("badlogic.jpg");
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        logo = new Logo(img);

    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        logo.resize(worldBounds);
    }

    @Override
    public void render(float delta) {

        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }


    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        logo.setNewPosition(new Vector2(touch.x - background.getHalfWidth(),touch.y - background.getHalfHeight()));
        System.out.println("touchDown screenX = " + touch.x + " screenY = " + touch.y);
        return super.touchDown(touch, pointer, button);
    }

    private void update(float delta) {

        logo.update(delta);

    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        logo.draw(batch);
        batch.end();
    }

}
