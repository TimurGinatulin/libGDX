package ru.ginatulin.pool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.ginatulin.base.SpritesPool;
import ru.ginatulin.math.Rect;
import ru.ginatulin.sprite.Kit;

public class KitPool extends SpritesPool<Kit> {
    private final Rect worldBounds;
    private final TextureRegion kitRegion;

    public KitPool(TextureRegion region, Rect worldBounds) {
        this.kitRegion = region;
        this.worldBounds = worldBounds;
    }

    @Override
    protected Kit newObject() {
        System.out.println(activeObject.size());
        return new Kit(kitRegion, worldBounds);
    }
}
