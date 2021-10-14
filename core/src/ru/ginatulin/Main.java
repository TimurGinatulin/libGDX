package ru.ginatulin;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private TextureRegion background;
    private Integer WINDOW_WIDTH = 1024;
    private Integer WINDOW_HEIGHT = 768;
    private boolean xIsMax;
    private boolean yIsMax;
    private int x, y = 0;

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        Gdx.graphics.setResizable(false);

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        background = new TextureRegion(
                new Texture("grass.jpg"), 0, 0, 1024, 768);
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0);
        ScreenUtils.clear(1, 0, 0, 1);
        batch.draw(img, x, y);
        changeCoordinate();
        batch.end();
    }

    @Override
    public void dispose() {
        background.getTexture().dispose();
        batch.dispose();
        img.dispose();
    }

    public void changeCoordinate() {
        if (x + img.getWidth() == WINDOW_WIDTH || x == -1)
            xIsMax = !xIsMax;
        if (y + img.getHeight() == WINDOW_HEIGHT || y == -1)
            yIsMax = !yIsMax;

        if (xIsMax)
            x--;
        else
            x++;
        if (yIsMax)
            y--;
        else
            y++;
    }
}
