package se.theslof.centipede.engine;

public class SpriteStatic extends Sprite {
    public SpriteStatic(SpriteMap spriteMap) {
        this(spriteMap, 0, 0);
    }

    public SpriteStatic(SpriteMap spriteMap, int mapX, int mapY) {
        this(spriteMap, mapX, mapY, spriteMap.getFrameWidth(), spriteMap.getFrameHeight());
    }

    public SpriteStatic(SpriteMap spriteMap, int mapX, int mapY, int width, int height) {
        super(spriteMap, mapX, mapY, width, height);
    }
}
