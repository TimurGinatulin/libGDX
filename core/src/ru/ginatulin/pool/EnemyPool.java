package ru.ginatulin.pool;

import ru.ginatulin.base.SpritesPool;
import ru.ginatulin.math.Rect;
import ru.ginatulin.sprite.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {
    private final BulletPool bulletPool;
    private final Rect worldBounds;

    public EnemyPool(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
    }

    @Override
    protected EnemyShip newObject() {
        System.out.println(freeObject.size()+"/"+activeObject.size());
        return new EnemyShip(bulletPool,worldBounds);
    }

    @Override
    public void dispose() {
        for (EnemyShip enemyShip: activeObject)
            enemyShip.dispose();
        for (EnemyShip enemyShip: freeObject)
            enemyShip.dispose();
        super.dispose();
    }
}
