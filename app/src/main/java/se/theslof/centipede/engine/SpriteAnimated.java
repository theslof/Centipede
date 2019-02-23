package se.theslof.centipede.engine;

import java.util.List;

public class SpriteAnimated extends Sprite {
    private long internalClock = 0;
    private long frameLengthInMilliseconds = 1000 / 60;
    private int currentFrame = 0;
    private List<SpriteAnimationFrame> animationSequence;
    private Repeat repeat = Repeat.REPEAT;
    private boolean reverse = false;

    public SpriteAnimated(SpriteMap spriteMap, List<SpriteAnimationFrame> animationSequence) {
        super(spriteMap, 0, 0, spriteMap.getFrameWidth(), spriteMap.getFrameHeight());
        this.animationSequence = animationSequence;
        update(0);
    }

    public SpriteAnimated(SpriteMap spriteMap, int width, int height, List<SpriteAnimationFrame> animationSequence) {
        super(spriteMap, 0, 0, width, height);
        this.animationSequence = animationSequence;
        update(0);
    }

    public void update(long frameTime) {
        // No animation frames, do nothing
        if (animationSequence == null || animationSequence.size() <= 0) { return; }

        long nextClock = internalClock + frameTime;
        int nextFrame = currentFrame + (int)(nextClock / frameLengthInMilliseconds);

        if (nextFrame >= animationSequence.size()) {
            // Next animation frame is outside of sequence, either loop or hold.
            switch (repeat) {
                case ONCE:
                    nextFrame = animationSequence.size() - 1;
                    break;
                case BOUNCE:
                    reverse = !reverse;
                case REPEAT:
                    nextFrame %= animationSequence.size();
                    break;
            }
        }

        internalClock = nextClock % frameLengthInMilliseconds;
        currentFrame = nextFrame;

        SpriteAnimationFrame animationFrame = reverse
                ? animationSequence.get(animationSequence.size() - nextFrame - 1)
                : animationSequence.get(nextFrame);

        this.getSubFrame().set(animationFrame.x, animationFrame.y,
                animationFrame.x + this.getSubFrame().width(),
                animationFrame.y + this.getSubFrame().height());
    }

    public List<SpriteAnimationFrame> getAnimationSequence() {
        return animationSequence;
    }

    public void setAnimationSequence(List<SpriteAnimationFrame> animationSequence) {
        this.animationSequence = animationSequence;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    public enum Repeat {
        REPEAT, ONCE, BOUNCE
    }
}
