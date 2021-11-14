package ru.ginatulin.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Font extends BitmapFont {
    private static StringBuilder inputText;
    private boolean isTyped = false;

    public Font(String fontFile, String imageFile) {
        super(Gdx.files.external(fontFile),
                Gdx.files.external(imageFile),
                false, false);
        inputText = new StringBuilder();
        getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
    }

    public void setSize(float size) {
        getData().setScale(size / getCapHeight());
    }

    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y
            , int align) {
        return super.draw(batch, str, x, y, 0, align, false);
    }
}
