package ru.ginatulin.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseShip;
import ru.ginatulin.math.Rect;
import ru.ginatulin.pool.BulletPool;

public class MainShip extends BaseShip {
    private final float SHIP_HEIGHT = 0.15f;
    private final float BOTTOM_MARGIN = 0.05f;
    private final float RELOAD_INTERVAL = 0.3f;
    private final int INVALID_POINTER = -1;
    private boolean isMoveRight = false;
    private boolean isMoveLeft = false;
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletRegions = atlas.findRegion("bulletMainShip");
        this.v = new Vector2();
        this.v0 = new Vector2(0.5f, 0);
        this.bulletV = new Vector2(0, 0.5f);
        this.bulletPosition = new Vector2();
        this.bulletPool = bulletPool;
        this.reloadInterval = RELOAD_INTERVAL;
        this.bulletHeight = 0.01f;
        this.bulletDamage = 1;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPosition.set(this.pos.x,getTop());
        if (getLeft() > worldBounds.getRight())
            setRight(worldBounds.getLeft());
        if (getRight() < worldBounds.getLeft())
            setLeft(worldBounds.getRight());
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

}
