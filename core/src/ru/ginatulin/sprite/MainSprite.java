package ru.ginatulin.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.Sprite;
import ru.ginatulin.math.Rect;

public class MainSprite extends Sprite {
    private static final float V_LEN = 0.0005f;
    private Vector2 v = new Vector2();
    private Vector2 touch = new Vector2();

    public MainSprite(Texture region) {
        super(new TextureRegion(region));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(worldBounds.getHeight() / 8);
    }

    @Override
    public void update(float delta) {
        if (pos.dst(touch) <= v.len())
            pos.set(touch);
        else
            pos.add(v);
        super.update(delta);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch.set(touch);
        v.set(touch.cpy().sub(pos)).setLength(V_LEN);
        return super.touchDown(touch, pointer, button);
    }
}
