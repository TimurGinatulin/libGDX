package ru.ginatulin.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.math.Rect;
import ru.ginatulin.pool.BulletPool;
import ru.ginatulin.sprite.Bullet;

public class BaseShip extends Sprite {

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
    private float reloadTimer;
    private final Sound shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
    protected float reloadInterval;

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
    }

    private void autoFire(float delta) {
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            shoot();
            reloadTimer = 0f;
        }
    }

    public void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegions, bulletPosition, bulletV, bulletHeight, worldBounds, bulletDamage);
        shoot.play();
    }
    public void dispose(){
        shoot.dispose();
    }

}
