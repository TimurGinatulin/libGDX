package ru.ginatulin.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.Sprite;
import ru.ginatulin.math.Rect;

public class MainShip extends Sprite {
    private Rect worldBounds;
    private final float SHEEP_SPEED = 0.005f;
    private boolean isMoveRight = false;
    private boolean isMoveLeft = false;
    private int pointer;

    public MainShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        regions[0].setRegion(
                regions[0].getRegionX(),
                regions[0].getRegionY(),
                regions[0].getRegionWidth() / 2,
                regions[0].getRegionHeight());
    }

    @Override
    public void update(float delta) {
        if (isMoveRight) {
            if (getRight() < worldBounds.getRight())
                pos.x += SHEEP_SPEED;
        }
        if (isMoveLeft) {
            if (getLeft() > worldBounds.getLeft())
                pos.x -= SHEEP_SPEED;
        }
        super.update(delta);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(worldBounds.getHeight() * 0.15f);
        setBottom(worldBounds.getBottom());
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.pointer = pointer;
        if (touch.x > 0)
            isMoveRight = true;
        if (touch.x < 0)
            isMoveLeft = true;
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (touch.x > 0 && this.pointer == pointer)
            isMoveRight = false;
        if (touch.x < 0 && this.pointer == pointer)
            isMoveLeft = false;
        return super.touchUp(touch, pointer, button);
    }

    public void keyDown(int keycode) {
        switch (keycode) {
            case 32: {
                isMoveRight = true;
                break;
            }
            case 29: {
                isMoveLeft = true;
                break;
            }
        }
    }

    public void keyUp(int keycode) {
        switch (keycode) {
            case 32: {
                isMoveRight = false;
                break;
            }
            case 29: {
                isMoveLeft = false;
                break;
            }
        }
    }
}
