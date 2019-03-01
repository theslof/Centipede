package se.theslof.centipede.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public abstract class Sprite implements SpriteEngine.Drawable {
    private SpriteMap spriteMap;
    private RectF frame = new RectF();
    private Rect subFrame = new Rect();
    private int zIndex = 0;
    private float rotation = 0;
    private boolean disabled = false;
    private boolean hidden = false;

    Sprite(SpriteMap spriteMap, int mapX, int mapY, int width, int height) {
        this.spriteMap = spriteMap;
        this.frame.set(0,0, width, height);
        int left = mapX * spriteMap.getFrameWidth();
        int top = mapY * spriteMap.getFrameHeight();
        this.subFrame.set(
                left,
                top,
                left + spriteMap.getFrameWidth(),
                top + spriteMap.getFrameHeight());
        Log.d("Sprite", "Created sprite: \nleft: " + this.subFrame.left
                + "\ntop: " + this.subFrame.top
                + "\nright: " + this.subFrame.right
                + "\nbottom: " + this.subFrame.bottom
        );
    }

    public void update(long frameTime) {
        // No animation frames, do nothing
    }

    public void draw(Canvas canvas) {
        if(disabled || hidden)
            return;
        Bitmap bitmap = Bitmap.createBitmap(this.spriteMap.getBitmap(), this.subFrame.left, this.subFrame.top, this.subFrame.width(), this.subFrame.height());
        Matrix matrix = new Matrix();
        matrix.postRotate((float)(180 / Math.PI * (rotation + Math.PI / 2)), bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
        matrix.postScale(this.frame.width() / this.subFrame.width(), this.frame.height() / this.subFrame.height());
        matrix.postTranslate(this.frame.left, this.frame.top);

        canvas.drawBitmap(bitmap, matrix, null);
    }

    public void moveTo(PointF point) {
        this.frame.set(
                point.x, point.y,
                point.x + this.frame.width(),
                point.y + this.frame.height());
    }

    public void moveCenter(PointF point) {
        this.frame.set(
                point.x - this.frame.width() / 2,
                point.y - this.frame.height() / 2,
                point.x + this.frame.width() / 2,
                point.y + this.frame.height() / 2);
    }

    public void moveBy(PointF offset) {
        this.frame.set(
                this.frame.left + offset.x,
                this.frame.top + offset.y,
                this.frame.right + offset.x,
                this.frame.bottom + offset.y);
    }

    public void setRotation(float angle) {
        this.rotation = angle;
    }

    public void rotateBy(float angle) {
        this.rotation += angle;
    }

    public float getRotation() {
        return rotation;
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

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
