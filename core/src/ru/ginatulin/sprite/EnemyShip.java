package ru.ginatulin.sprite;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseShip;
import ru.ginatulin.math.Rect;
import ru.ginatulin.pool.BulletPool;

public class EnemyShip extends BaseShip {
    public EnemyShip(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.v = new Vector2();
        this.v0 = new Vector2();
        this.bulletV = new Vector2();
        this.bulletPosition = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPosition.set(this.pos.x,getBottom());
        if (getBottom()< worldBounds.getBottom())
            destroy();
    }

    public void set(
            TextureRegion [] regions,
            Vector2 v,
            TextureRegion bulletRegion,
            float bulletHeight,
            Vector2 bulletV,
            int damage,
            int hp,
            float reloadInterval,
            float height
    ){
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
}
