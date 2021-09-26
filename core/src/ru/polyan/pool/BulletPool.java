package ru.polyan.pool;

import ru.polyan.base.SpritesPool;
import ru.polyan.sprite.Bullet;
import ru.polyan.utils.EnemyEmitter;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

}
