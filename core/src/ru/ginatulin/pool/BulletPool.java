package ru.ginatulin.pool;

import ru.ginatulin.base.SpritesPool;
import ru.ginatulin.sprite.Bullet;

public class BulletPool extends SpritesPool<Bullet> {
    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
