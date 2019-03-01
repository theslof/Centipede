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

    @Override
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

        int left = animationFrame.x * this.getSpriteMap().getFrameWidth();
        int top = animationFrame.y * this.getSpriteMap().getFrameHeight();
        this.getSubFrame().set(left, top,
                left + this.getSubFrame().width(),
                top + this.getSubFrame().height());
    }

    public List<SpriteAnimationFrame> getAnimationSequence() {
        return animationSequence;
    }

    public void setAnimationSequence(List<SpriteAnimationFrame> animationSequence) {
        this.animationSequence = animationSequence;
    }

    public long getFrameLength() {
        return frameLengthInMilliseconds;
    }

    public void setFrameLength(long milliseconds) {
        this.frameLengthInMilliseconds = milliseconds;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public enum Repeat {
        REPEAT, ONCE, BOUNCE
    }
}
