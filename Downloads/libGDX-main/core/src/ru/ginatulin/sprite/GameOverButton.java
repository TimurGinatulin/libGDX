package ru.ginatulin.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseButton;
import ru.ginatulin.math.Rect;

public class GameOverButton extends BaseButton {
    private final float BUTTON_HEIGHT = 0.05f;
    private static final float PADDING = 0.02f;
    private final float ANIMATED_INTERVAL = 0.65f;
    private float animatedTimer = 0.0f;
    private boolean isActive;
    private boolean isSelected;

    public GameOverButton(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
        this.isSelected = false;
        this.isActive = false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (isActive)
            super.draw(batch);
    }

    @Override
    public void update(float delta) {
        if (isSelected && isActive) {
            if (animatedTimer < ANIMATED_INTERVAL / 2 && this.scale > 0.79f)
                this.scale -= 0.012f;
            if (animatedTimer > ANIMATED_INTERVAL / 2 && this.scale < 1.0f)
                this.scale += 0.012f;
            if (animatedTimer >= ANIMATED_INTERVAL)
                animatedTimer = 0.0f;
            animatedTimer += delta;
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return super.touchUp(touch, pointer, button);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(BUTTON_HEIGHT);
        setBottom(worldBounds.getHalfHeight()/2 + PADDING);
    }

    public boolean mouseMoved(Vector2 touch) {
        if (isMe(touch))
            isSelected = true;
        else {
            isSelected = false;
            this.scale = 1.0f;
        }
        return false;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
