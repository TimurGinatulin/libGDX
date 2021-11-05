package ru.ginatulin.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.Sprite;

public class Explosion extends Sprite {
    private static final float ANIMATED_INTERVAL = 0.017f;
    private Sound explosionSound;
    private float animateTimer;

    public Explosion(TextureAtlas atlas, Sound explosionSound) {
        super(atlas.findRegion("explosion"), 9, 9, 74);
        this.explosionSound = explosionSound;
    }

    public void set(Vector2 position, float height) {
        explosionSound.play(0.05f);
        this.pos.set(position);
        setHeightProportion(height);
    }

    @Override
    public void update(float delta) {
        animateTimer+= delta;
        if (animateTimer >= ANIMATED_INTERVAL){
            animateTimer = 0f;
            if (++frame == regions.length)
                destroy();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }
}
