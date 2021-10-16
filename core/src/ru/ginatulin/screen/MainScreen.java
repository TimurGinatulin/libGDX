package ru.ginatulin.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseScreen;

public class MainScreen extends BaseScreen {
    private Vector2 touch;
    private Texture img;
    private TextureRegion background;
    private Integer ANT_WIDTH = 128;
    private Integer ANT_HEIGHT = 128;

    @Override
    public void show() {
        super.show();
        touch = new Vector2();
        img = new Texture("ant2.png");
        background = new TextureRegion(
                new Texture("grass.jpg"), 0, 0, 1024, 768);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(img, touch.x, touch.y);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        background.getTexture().dispose();
        img.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX - 64, Gdx.graphics.getHeight() - screenY - 64);
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
