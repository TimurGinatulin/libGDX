package ru.ginatulin.sprite;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseShip;
import ru.ginatulin.math.Rect;
import ru.ginatulin.pool.BulletPool;
import ru.ginatulin.pool.ExplosionPool;

public class EnemyShip extends BaseShip {
    private Vector2 startingBoost = new Vector2(0f, -0.7f);

    public EnemyShip(BulletPool bulletPool, Rect worldBounds, ExplosionPool explosionPool) {
        this.explosionPool = explosionPool;
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.v = new Vector2();
        this.v0 = new Vector2();
        this.bulletV = new Vector2();
        this.bulletPosition = new Vector2();
    }

    @Override
    public void update(float delta) {
        if (getTop() > worldBounds.getTop()) {
            pos.mulAdd(startingBoost, delta);
            reloadTimer = reloadInterval * 0.8f;
        }
        super.update(delta);
        bulletPosition.set(this.pos.x, getBottom());
        if (getBottom() < worldBounds.getBottom())
            destroy();
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v,
            TextureRegion bulletRegion,
            float bulletHeight,
            Vector2 bulletV,
            int damage,
            int hp,
            float reloadInterval,
            float height
    ) {
        this.regions = regions;
        this.v.set(v);
        this.bulletRegions = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(bulletV);
        this.bulletDamage = damage;
        this.hp = hp;
        this.reloadInterval = reloadInterval;
        setHeightProportion(height);
    }

    @Override
    protected void autoFire(float delta) {
        super.autoFire(delta);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public boolean isBulletCollision(Bullet bullet) {
        return !(bullet.getRight() < getLeft() ||
                bullet.getLeft() > getRight() ||
                bullet.getBottom() > getTop() ||
                bullet.getTop() < pos.y);
    }
}
