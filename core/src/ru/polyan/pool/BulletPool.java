package ru.polyan.pool;

import ru.polyan.base.SpritesPool;
import ru.polyan.sprite.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

}
