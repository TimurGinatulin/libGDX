package ru.ginatulin.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Regions {
    public static TextureRegion[] split(TextureRegion region, int rows, int cols, int frames) {
        if (region == null)
            throw new RuntimeException("Split null region");
        TextureRegion[] regions = new TextureRegion[frames];
        int titleWidth = region.getRegionWidth() / cols;
        int titleHeight = region.getRegionHeight() / rows;
        int frame = 0;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                regions[frame] = new TextureRegion(region, titleWidth * j,
                        titleHeight * i, titleWidth, titleHeight);
                if (frame == frames - 1)
                    return regions;
                frame++;
            }
        }
        return regions;
    }
}
