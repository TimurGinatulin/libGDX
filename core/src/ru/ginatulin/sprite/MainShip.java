package ru.ginatulin.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.Sprite;
import ru.ginatulin.math.Rect;
import ru.ginatulin.pool.BulletPool;

public class MainShip extends Sprite {
    private Rect worldBounds;
    private final float SHIP_HEIGHT = 0.15f;
    private final float BOTTOM_MARGIN = 0.05f;
    private final Vector2 v;
    private final Vector2 v0;


    private final int INVALID_POINTER = -1;
    private boolean isMoveRight = false;
    private boolean isMoveLeft = false;
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    private final BulletPool bulletPool;
    private final TextureRegion bulletRegions;
    private final Vector2 bulletV;
    private final float bulletHeight;
    private final int bulletDamage;
    private final Sound shoot;
    private float shootSpeed = 0.5f;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletRegions = atlas.findRegion("bulletMainShip");
        v = new Vector2();
        v0 = new Vector2(0.5f, 0);
        bulletV = new Vector2(0, 0.5f);
        this.bulletPool = bulletPool;
        bulletHeight = 0.01f;
        bulletDamage = 1;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        autoFire(delta);
        if (getLeft() > worldBounds.getRight())
            setRight(worldBounds.getLeft());
        if (getRight() < worldBounds.getLeft())
            setLeft(worldBounds.getRight());
    }

    private void autoFire(float delta) {
        shootSpeed -= delta;
        if (shootSpeed < 0) {
            shoot();
            shootSpeed = 0.5f;
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(SHIP_HEIGHT);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }


    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x > 0) {
            if (rightPointer != INVALID_POINTER)
                return false;
            rightPointer = pointer;
            moveRight();
            isMoveRight = true;
        }
        if (touch.x < 0) {
            if (leftPointer != INVALID_POINTER)
                return false;
            leftPointer = pointer;
            moveLeft();
            isMoveRight = true;
        }
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER)
                moveRight();
            else
                stop();
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER)
                moveLeft();
            else
                stop();
        }
        return super.touchUp(touch, pointer, button);
    }

    public void keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT:
            case Input.Keys.D: {
                isMoveRight = true;
                moveRight();
                break;
            }
            case Input.Keys.LEFT:
            case Input.Keys.A: {
                isMoveLeft = true;
                moveLeft();
                break;
            }
        }
    }

    public void keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                isMoveRight = false;
                if (isMoveLeft)
                    moveLeft();
                else
                    stop();
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                isMoveLeft = false;
                if (isMoveRight)
                    moveRight();
                else
                    stop();
                break;
            case Input.Keys.W:
            case Input.Keys.UP:
                shoot();
                break;
        }
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotateDeg(180);
    }

    private void stop() {
        v.setZero();
    }

    public void shoot() {
        shoot.play();
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegions, this.pos, bulletV, bulletHeight, worldBounds, bulletDamage);
    }
}
