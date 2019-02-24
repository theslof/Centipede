package se.theslof.centipede.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.ArrayList;
import java.util.List;

public class SpriteEngine {
    private int backgroundColor = Color.WHITE;
    private List<SpriteLayer> layers = new ArrayList<>();


    public void draw(Canvas canvas, long frameTime) {
        canvas.drawColor(backgroundColor);

        layers.forEach(layer -> {
            layer.getDrawables().forEach(drawable -> {
                drawable.update(frameTime);
                drawable.draw(canvas);
            });
        });
    }

    public void addLayer(SpriteLayer layer) {
        layers.add(layer);
        layers.sort((SpriteLayer s1, SpriteLayer s2) -> s1.getzIndex() - s2.getzIndex());
    }

    @ColorInt
    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<SpriteLayer> getLayers() {
        return layers;
    }

    public interface Drawable {
        void update(long frameTime);
        void draw(Canvas canvas);
        int getzIndex();
    }
}
