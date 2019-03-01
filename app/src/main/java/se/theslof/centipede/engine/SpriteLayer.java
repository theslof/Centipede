package se.theslof.centipede.engine;

import java.util.ArrayList;
import java.util.List;

public class SpriteLayer {
    private int zIndex;
    private String identifier;
    private List<SpriteEngine.Drawable> drawables;
    private boolean hidden = false;
    private boolean disabled = false;

    public SpriteLayer() {
        this(0, null);
    }

    public SpriteLayer(String identifier) {
        this(0, identifier);
    }

    public SpriteLayer(int zIndex) {
        this(zIndex, null);
    }

    public SpriteLayer(int zIndex, String identifier) {
        this.zIndex = zIndex;
        this.identifier = identifier;
        drawables = new ArrayList<>();
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<SpriteEngine.Drawable> getDrawables() {
        return drawables;
    }

    public void setDrawables(List<SpriteEngine.Drawable> drawables) {
        this.drawables = drawables;
    }

    public void addDrawable(SpriteEngine.Drawable drawable) {
        drawables.add(drawable);
        drawables.sort((SpriteEngine.Drawable s1, SpriteEngine.Drawable s2) -> s1.getzIndex() - s2.getzIndex());
    }

    public void removeDrawable(SpriteEngine.Drawable drawable) {
        drawables.remove(drawable);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
