package se.theslof.centipede.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class SpriteMap {
    private Bitmap bitmap;
    private int width;
    private int height;
    private int frameWidth;
    private int frameHeight;

    public SpriteMap(Resources res, int resID, int width, int height, int frameWidth, int frameHeight) {
        this(
                Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(res, resID), width, height, false),
                width,
                height,
                frameWidth,
                frameHeight);
    }

    public SpriteMap(Bitmap bitmap, int width, int height, int frameWidth, int frameHeight) {
        this.bitmap = bitmap;

        this.width = width;
        this.height = height;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public void drawFrame(Canvas canvas, int left, int top, int frameIndex) {

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }
}
