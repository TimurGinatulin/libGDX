package ru.ginatulin.util;

import ru.ginatulin.math.Rect;
import ru.ginatulin.math.Rnd;
import ru.ginatulin.pool.KitPool;
import ru.ginatulin.sprite.Kit;

public class KitEmitter {
    private float generateTimer;
    private static final float GENERATE_INTERVAL = 1.5f;

    private final Rect worldBounds;
    private final KitPool kitPool;

    public KitEmitter(KitPool kitPool, Rect worldBounds) {
        this.worldBounds = worldBounds;
        this.kitPool = kitPool;
    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= GENERATE_INTERVAL) {
            generateTimer = 0f;
            Kit kit = kitPool.obtain();
            kit.set(0.02f);
            kit.pos.x = Rnd.nextFloat(
                    worldBounds.getLeft() + kit.getHalfWidth(),
                    worldBounds.getRight() - kit.getHalfWidth());
            kit.pos.y = worldBounds.getTop();
        }
    }
}
