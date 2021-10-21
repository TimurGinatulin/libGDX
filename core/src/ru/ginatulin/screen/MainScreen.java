package ru.ginatulin.screen;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseScreen;
import ru.ginatulin.math.Rect;
import ru.ginatulin.sprite.Background;
import ru.ginatulin.sprite.MainSprite;

public class MainScreen extends BaseScreen {
    private Background background;
    private Texture bg;
    private MainSprite main;
    private Texture mn;

    @Override
    public void show() {
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        mn = new Texture("badlogic.jpg");
        main = new MainSprite(mn);
        super.show();

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        main.draw(batch);
        main.update(delta);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        main.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        mn.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        main.touchDown(touch,pointer,button);
        return super.touchDown(touch, pointer, button);
    }
}
