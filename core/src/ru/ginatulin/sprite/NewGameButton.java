package ru.ginatulin.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseButton;
import ru.ginatulin.math.Rect;
import ru.ginatulin.screen.GameScreen;

public class NewGameButton extends BaseButton {
    private final float BUTTON_HEIGHT = 0.05f;
    private final float PADDING = 0.02f;
    private final float ANIMATED_INTERVAL = 0.65f;
    private float animatedTimer = 0.0f;
    private boolean isActive;
    private boolean isSelected;
    private final GameScreen screen;

    public NewGameButton(TextureAtlas atlas, GameScreen screen) {
        super(atlas.findRegion("button_new_game"));
        this.isSelected = false;
        this.isSelected = false;
        this.screen = screen;
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

    public boolean mouseMoved(Vector2 touch) {
        if (isMe(touch))
            isSelected = true;
        else {
            isSelected = false;
            this.scale = 1.0f;
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (isActive)
            super.draw(batch);
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
      this.screen.rePlay();
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(BUTTON_HEIGHT);
        setTop(worldBounds.getHalfHeight()/2-PADDING);
    }
}
