package se.theslof.centipede.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.ColorInt;

public class Label implements SpriteEngine.Drawable {
    private @ColorInt int textColor;
    private int fontSize;
    private String text;
    private Point position;
    private int zIndex = 0;
    private boolean disabled;
    private boolean hidden;

    public Label(String text) {
        this(text, new Point(), Color.BLACK, 12);
    }

    public Label(String text, Point position) {
        this(text, position, Color.BLACK, 12);
    }

    public Label(String text, Point position, @ColorInt int textColor) {
        this(text, position, textColor, 12);
    }

    public Label(String text, Point position, @ColorInt int textColor, int fontSize) {
        this.textColor = textColor;
        this.fontSize = fontSize;
        this.text = text;
        this.position = position;
    }

    @Override
    public void update(long frameTime) { }

    @Override
    public void draw(Canvas canvas) {
        if(disabled || hidden)
            return;
        Paint paint = new Paint();
        paint.setColor(textColor);
        paint.setTextSize(fontSize);
        canvas.drawText(text, position.x, position.y, paint);
    }

    @ColorInt
    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
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
