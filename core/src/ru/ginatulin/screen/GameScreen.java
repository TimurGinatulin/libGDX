package ru.ginatulin.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseScreen;
import ru.ginatulin.math.Rect;
import ru.ginatulin.sprite.Background;
import ru.ginatulin.sprite.ExitButton;
import ru.ginatulin.sprite.PlayButton;
import ru.ginatulin.sprite.Star;

public class GameScreen extends BaseScreen {
    private static final int STAR_COUNT = 64;
    private Background background;
    private Texture bg;
    private Star[] stars;
    private TextureAtlas atlas;

    @Override
    public void show() {
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }
    private void update(float delta) {
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].update(delta);
        }
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].draw(batch);
        }
        batch.end();
    }
    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].resize(worldBounds);
        }
        super.resize(worldBounds);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return super.touchUp(touch, pointer, button);
    }
}
