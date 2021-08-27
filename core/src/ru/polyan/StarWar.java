package ru.polyan;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarWar extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture img2;
	float x = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		img2 = new Texture("car.jpg");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(img2, x, 300);
		batch.end();
		x++;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
