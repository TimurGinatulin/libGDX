package ru.ginatulin.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.ginatulin.base.BaseScreen;
import ru.ginatulin.math.Rect;
import ru.ginatulin.pool.BulletPool;
import ru.ginatulin.pool.EnemyPool;
import ru.ginatulin.sprite.Background;
import ru.ginatulin.sprite.Bullet;
import ru.ginatulin.sprite.EnemyShip;
import ru.ginatulin.sprite.MainShip;
import ru.ginatulin.sprite.Star;
import ru.ginatulin.util.EnemyEmitter;

public class GameScreen extends BaseScreen {
    private int killCounter = 0;
    private static final int STAR_COUNT = 64;
    private TextureAtlas atlas;
    private Background background;
    private Texture bg;
    private Star[] stars;
    private MainShip mainShip;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemyEmitter enemyEmitter;
    private Sound explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));

    @Override
    public void show() {
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        stars = new Star[STAR_COUNT];
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool, worldBounds);
        mainShip = new MainShip(atlas, bulletPool);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }
        enemyEmitter = new EnemyEmitter(enemyPool, atlas, worldBounds);
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        freeAllDestroyed();
        draw();
    }

    private void update(float delta) {
        mainShip.update(delta);
        bulletPool.updateActiveObject(delta);
        enemyPool.updateActiveObject(delta);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].update(delta);
        }
        enemyEmitter.generate(delta);
        collisionDestroyed();
    }

    private void collisionDestroyed() {
        for (EnemyShip enemyShip : enemyPool.getActiveObject()) {
            if (!enemyShip.isOutside(mainShip))
                enemyShip.destroy();
        }
        for (Bullet bullet : bulletPool.getActiveObject()) {
            for (EnemyShip enemyShip : enemyPool.getActiveObject()) {
                if (!enemyShip.isOutside(bullet)) {
                    enemyShip.destroy();
                    explosion.play(0.1f);
                    bullet.destroy();
                    killCounter++;
                    Gdx.graphics.setTitle(String.valueOf(killCounter));
                }
            }

        }
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].draw(batch);
        }
        bulletPool.drawActiveObject(batch);
        enemyPool.drawActiveObject(batch);

        mainShip.draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        mainShip.resize(worldBounds);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].resize(worldBounds);
        }
        super.resize(worldBounds);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosion.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch, pointer, button);
        return super.touchUp(touch, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return super.keyUp(keycode);
    }
}
