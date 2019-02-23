package se.theslof.centipede.engine;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Sprite {
    private SpriteMap spriteMap;
    private int mapX;
    private int mapY;
    private float width;
    private float height;
    private float x = 0;
    private float y = 0;
    private int zIndex = 0;

    public Sprite(SpriteMap spriteMap, int mapX, int mapY) {
        this(spriteMap, mapX, mapY, spriteMap.getFrameWidth(), spriteMap.getFrameHeight());
    }

    public Sprite(SpriteMap spriteMap, int mapX, int mapY, int width, int height) {
        this.spriteMap = spriteMap;
        this.mapX = mapX;
        this.mapY = mapY;
        this.width = width;
        this.height = height;
    }

    public void draw(Canvas canvas) {
        Rect frameToDraw = new Rect(
                mapX,
                mapY,
                mapX + spriteMap.getFrameWidth(),
                mapY + spriteMap.getFrameHeight());
        RectF whereToDraw = new RectF(
                x,
                y,
                x + width,
                y + height);

        canvas.drawBitmap(spriteMap.getBitmap(),
                frameToDraw,
                whereToDraw, null);
    }

    public void moveTo(PointF point) {
        this.x = point.x;
        this.y = point.y;
    }

    public void moveBy(PointF offset) {
        this.x += offset.x;
        this.y += offset.y;
    }

    public SpriteMap getSpriteMap() {
        return spriteMap;
    }

    public void setSpriteMap(SpriteMap spriteMap) {
        this.spriteMap = spriteMap;
    }

    public int getMapX() {
        return mapX;
    }

    public void setMapX(int mapX) {
        this.mapX = mapX;
    }

    public int getMapY() {
        return mapY;
    }

    public void setMapY(int mapY) {
        this.mapY = mapY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
