package ru.ginatulin.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.Sprite;
import ru.ginatulin.math.Rect;

public class Kit extends Sprite {
    protected Rect worldBounds;
    private Integer damage = 100;
    protected Vector2 v = new Vector2(0f, -0.2f);

    public Kit(TextureRegion region, Rect worldBounds) {
        super(region);
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (getBottom() < worldBounds.getBottom())
            destroy();
    }

    public void set(
            float height
    ) {
        setHeightProportion(height);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public int getDamage() {
        destroy();
        return -damage;
    }

}
