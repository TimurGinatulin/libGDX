package ru.ginatulin.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseScreen;

public class MainScreen extends BaseScreen {
    private Vector2 antPosition;
    private Vector2 antDirection;
    private Vector2 antDist;
    private Texture ant;
    private TextureRegion background;

    @Override
    public void show() {
        super.show();
        antDirection = new Vector2();
        antPosition = new Vector2();
        antDist = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        ant = new Texture("ant2.png");
        background = new TextureRegion(
                new Texture("grass.jpg"), 0, 0, 1024, 768);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        batch.draw(background, 0, 0);
        checkPosition();
        batch.draw(ant, antPosition.x, antPosition.y);
        batch.end();
    }

    private void checkPosition() {
        if (antPosition.x != antDist.x || antPosition.y != antDist.y) {
            antDirection.set(antPosition.x - antDist.x, antPosition.y - antDist.y);
            antPosition.sub(antDirection.nor());
        }else {
            antPosition = antDist;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        background.getTexture().dispose();
        ant.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        antDist.set(screenX - 64, Gdx.graphics.getHeight() - screenY - 64);
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
