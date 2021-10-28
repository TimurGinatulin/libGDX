package ru.ginatulin.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritesPool<T extends Sprite> {
    protected final List<T> activeObject = new ArrayList<>();
    protected final List<T> freeObject = new ArrayList<>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        if (freeObject.isEmpty())
            object = newObject();
        else
            object = freeObject.remove(freeObject.size() - 1);
        activeObject.add(object);
        return object;
    }

    public void updateActiveObject(float delta) {
        for (T object : activeObject) {
            if (!object.isDestroyed())
                object.update(delta);
        }
    }

    public void freeAllDestroyed() {
        for (int i = 0; i < activeObject.size() - 1; i++) {
            T object = activeObject.get(i);
            if (object.isDestroyed()) {
                free(object);
                i--;
            }
        }
    }

    public void drawActiveObject(SpriteBatch batch) {
        for (T object : activeObject) {
            if (!object.isDestroyed())
                object.draw(batch);
        }
    }

    public List<T> getActiveObject() {
        return activeObject;
    }

    public void dispose() {
        activeObject.clear();
        freeObject.clear();
    }

    public void free(T object) {
        object.flushDestroy();
        if (activeObject.remove(object))
            freeObject.add(object);
    }
}
