package ru.ginatulin.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.math.Rect;
import ru.ginatulin.pool.BulletPool;
import ru.ginatulin.pool.ExplosionPool;
import ru.ginatulin.sprite.Bullet;
import ru.ginatulin.sprite.Explosion;

public class BaseShip extends Sprite {
    private static final float DAMAGE_ANIMATED_INTERVAL = 0.1f;
    protected ExplosionPool explosionPool;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegions;
    protected Vector2 bulletV;
    protected float bulletHeight;
    protected int bulletDamage;
    protected Vector2 bulletPosition;
    protected int hp;

    protected Vector2 v;
    protected Vector2 v0;

    protected Rect worldBounds;
    protected float reloadTimer =
            Float.MAX_VALUE;
    private final Sound shoot =
            Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
    protected float reloadInterval;
    private float damageAnimateTimer = DAMAGE_ANIMATED_INTERVAL;

    public BaseShip() {
    }

    public BaseShip(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        this.hp = 100;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        autoFire(delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATED_INTERVAL)
            frame = 0;
    }

    protected void autoFire(float delta) {
        if (this.reloadTimer >= this.reloadInterval) {
            shoot();
            reloadTimer = 0f;
        }
        reloadTimer += delta;
    }

    public void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegions, bulletPosition, bulletV, bulletHeight, worldBounds, bulletDamage);
        shoot.play();
    }

    public void damage(int dmg) {
        this.hp -= dmg;
        if (this.hp <= 0) {
            this.hp = 0;
            destroy();
        }
        damageAnimateTimer = 0f;
        frame = 1;
    }

    public int getDamage() {
        return bulletDamage;
    }

    public void dispose() {
        shoot.dispose();
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    private void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(this.pos,getHeight());
    }
}
