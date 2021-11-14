package ru.ginatulin.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import ru.ginatulin.base.BaseScreen;
import ru.ginatulin.base.Font;
import ru.ginatulin.math.Rect;
import ru.ginatulin.pool.BulletPool;
import ru.ginatulin.pool.EnemyPool;
import ru.ginatulin.pool.ExplosionPool;
import ru.ginatulin.sprite.Background;
import ru.ginatulin.sprite.Bullet;
import ru.ginatulin.sprite.EnemyShip;
import ru.ginatulin.sprite.GameOverButton;
import ru.ginatulin.sprite.MainShip;
import ru.ginatulin.sprite.NewGameButton;
import ru.ginatulin.sprite.Star;
import ru.ginatulin.util.EnemyEmitter;

public class GameScreen extends BaseScreen {
    private int frags = 0;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private static final float FONT_SIZE = 0.02f;
    private static final float MARGIN = 0.01f;
    private ExplosionPool explosionPool;
    private static final int STAR_COUNT = 64;
    private TextureAtlas atlas;
    private Background background;
    private Texture bg;
    private Star[] stars;
    private MainShip mainShip;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemyEmitter enemyEmitter;
    private Sound explosionSound;
    private NewGameButton newGameButton;
    private GameOverButton gameOverButton;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

    @Override
    public void show() {
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
        bg = new Texture("textures/bg.png");
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        background = new Background(bg);
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        gameOverButton = new GameOverButton(atlas);
        explosionPool = new ExplosionPool(atlas, explosionSound);
        stars = new Star[STAR_COUNT];
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool, worldBounds, explosionPool);
        mainShip = new MainShip(atlas, bulletPool, explosionPool);
        newGameButton = new NewGameButton(atlas, this);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }
        enemyEmitter = new EnemyEmitter(enemyPool, atlas, worldBounds);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
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
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].update(delta);
        }
        if (!mainShip.isDestroyed()) {
            mainShip.update(delta);
            bulletPool.updateActiveObject(delta);

            enemyPool.updateActiveObject(delta);
            enemyEmitter.generate(delta,frags);
        }
        explosionPool.updateActiveObject(delta);
        newGameButton.update(delta);
        gameOverButton.update(delta);
        collisionDestroyed();
    }

    private void collisionDestroyed() {
        if (mainShip.isDestroyed())
            return;
        for (EnemyShip enemyShip : enemyPool.getActiveObject()) {
            float minDist = mainShip.getWidth();
            if (mainShip.pos.dst(enemyShip.pos) < minDist) {
                enemyShip.destroy();
                mainShip.damage(enemyShip.getDamage());
            }
        }
        for (Bullet bullet : bulletPool.getActiveObject()) {

            if (bullet.getOwner() != mainShip) {
                if (mainShip.isBulletCollision(bullet)) {
                    mainShip.damage(bullet.getDamage());
                    bullet.destroy();
                    Gdx.graphics.setTitle(String.valueOf(mainShip.getHP()));
                }
                continue;
            }
            for (EnemyShip enemyShip : enemyPool.getActiveObject()) {

                if (enemyShip.isBulletCollision(bullet)) {
                    enemyShip.damage(bullet.getDamage());
                    if (enemyShip.isDestroyed())
                        frags++;
                    bullet.destroy();
                }
            }
        }
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].draw(batch);
        }
        if (!mainShip.isDestroyed()) {
            bulletPool.drawActiveObject(batch);
            enemyPool.drawActiveObject(batch);
            mainShip.draw(batch);
        } else {
            gameOverButton.setActive(true);
            newGameButton.setActive(true);
        }
        explosionPool.drawActiveObject(batch);
        newGameButton.draw(batch);
        gameOverButton.draw(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags),
                worldBounds.getLeft() + MARGIN, worldBounds.getTop() - MARGIN);
        sbHp.setLength(0);
        font.draw(batch, sbHp.append(HP).append(mainShip.getHP()),
                 worldBounds.pos.x,worldBounds.getTop() - MARGIN, Align.center);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()),
                worldBounds.getRight() - MARGIN, worldBounds.getTop() - MARGIN,Align.topRight);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        mainShip.resize(worldBounds);
        newGameButton.resize(worldBounds);
        gameOverButton.resize(worldBounds);
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
        explosionPool.dispose();
        explosionSound.dispose();
        font.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch, pointer, button);
        gameOverButton.touchDown(touch, pointer, button);
        newGameButton.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch, pointer, button);
        gameOverButton.touchUp(touch, pointer, button);
        newGameButton.touchUp(touch, pointer, button);
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

    @Override
    public boolean mouseMoved(Vector2 touch) {
        gameOverButton.mouseMoved(touch);
        newGameButton.mouseMoved(touch);
        return super.mouseMoved(touch);
    }

    public void rePlay() {
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        gameOverButton.setActive(false);
        newGameButton.setActive(false);
        mainShip.pos.x = 0.0f;
        mainShip.reborn();
    }
}
