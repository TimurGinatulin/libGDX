package ru.ginatulin.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.ginatulin.base.SpritesPool;
import ru.ginatulin.sprite.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {
    private final TextureAtlas atlas;
    private final Sound explosionSound;

    public ExplosionPool(TextureAtlas atlas,Sound explosionSound) {
        this.explosionSound = explosionSound;
        this.atlas = atlas;
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(atlas,explosionSound);
    }
}
