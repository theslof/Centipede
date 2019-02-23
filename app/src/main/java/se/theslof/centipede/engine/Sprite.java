package se.theslof.centipede.engine;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public abstract class Sprite {
    private SpriteMap spriteMap;
    private RectF frame = new RectF();
    private Rect subFrame = new Rect();
    private int zIndex = 0;

    Sprite(SpriteMap spriteMap, int mapX, int mapY, int width, int height) {
        this.spriteMap = spriteMap;
        this.frame.set(0,0, width, height);
        this.subFrame.set(mapX, mapY, spriteMap.getFrameWidth(), spriteMap.getFrameHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(spriteMap.getBitmap(),
                subFrame,
                frame, null);
    }

    public void moveTo(PointF point) {
        this.frame.set(
                point.x, point.y,
                point.x + this.frame.width(),
                point.y + this.frame.height());
    }

    public void moveBy(PointF offset) {
        this.frame.set(
                this.frame.left + offset.x,
                this.frame.top + offset.y,
                this.frame.right + offset.x,
                this.frame.bottom + offset.y);
    }

    public SpriteMap getSpriteMap() {
        return spriteMap;
    }

    public void setSpriteMap(SpriteMap spriteMap) {
        this.spriteMap = spriteMap;
    }

    public RectF getFrame() {
        return frame;
    }

    public void setFrame(RectF frame) {
        this.frame = frame;
    }

    public Rect getSubFrame() {
        return subFrame;
    }

    public void setSubFrame(Rect frame) {
        this.subFrame = frame;
    }

    public void setSize(float width, float height) {
        this.frame.right = this.frame.left + width;
        this.frame.bottom = this.frame.top + height;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
