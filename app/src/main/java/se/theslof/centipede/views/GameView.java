package se.theslof.centipede.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.theslof.centipede.R;
import se.theslof.centipede.engine.Label;
import se.theslof.centipede.engine.Sprite;
import se.theslof.centipede.engine.SpriteAnimated;
import se.theslof.centipede.engine.SpriteAnimationFrame;
import se.theslof.centipede.engine.SpriteEngine;
import se.theslof.centipede.engine.SpriteLayer;
import se.theslof.centipede.engine.SpriteMap;
import se.theslof.centipede.engine.SpriteStatic;

public class GameView extends SurfaceView implements Runnable {
    Thread mainThread = null;
    SurfaceHolder holder;
    volatile boolean playing;
    Canvas canvas;
    long fps = 1;
    private long frameTime;
    SpriteEngine engine = new SpriteEngine();
    List<Sprite> centipede;
    Label fpsLabel;
    boolean isMoving = true;
    float speed = 250;
    float maxTurnSpeedPerSecond = (float)Math.PI * 1.5f;
    float targetAngle = (float)(0);

    long timeToNextApple = 0;

    SpriteLayer backgroundLayer = new SpriteLayer(0, "background");
    SpriteLayer gameLayer = new SpriteLayer(1, "game");
    SpriteLayer guiLayer = new SpriteLayer(2, "gui");

    Sprite centipedeHeadSprite;

    List<SpriteAnimationFrame> bodyAnimationSequence = Arrays.asList(
            new SpriteAnimationFrame(1,0),
            new SpriteAnimationFrame(2,0),
            new SpriteAnimationFrame(3,0),
            new SpriteAnimationFrame(4,0),
            new SpriteAnimationFrame(5,0),
            new SpriteAnimationFrame(6,0),
            new SpriteAnimationFrame(7,0),
            new SpriteAnimationFrame(8,0)
    );

    SpriteMap appleMap = new SpriteMap(this.getResources(), R.drawable.apple, 64, 64, 64, 64);

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        holder = getHolder();

        engine.addLayer(backgroundLayer);
        engine.addLayer(gameLayer);
        engine.addLayer(guiLayer);

        centipede = new ArrayList<>();

        SpriteMap centipedeMap = new SpriteMap(this.getResources(), R.drawable.centipede, 1280, 128, 128, 128);
        centipedeHeadSprite = new SpriteStatic(centipedeMap);
        centipedeHeadSprite.setzIndex(0);
        centipedeHeadSprite.setRotation(targetAngle);
        gameLayer.addDrawable(centipedeHeadSprite);
        centipede.add(centipedeHeadSprite);
        centipedeHeadSprite.moveCenter(new PointF(400, 600));

        Sprite centipedeTailSprite = new SpriteStatic(centipedeMap, 9, 0);
        centipedeTailSprite.setzIndex(-1);
        centipedeTailSprite.setRotation(targetAngle);
        gameLayer.addDrawable(centipedeTailSprite);
        centipede.add(centipedeTailSprite);
        centipedeTailSprite.moveCenter(new PointF(400, 1450));

        for (int i = 1; i < 9; i++) {
            addBodySegment();
        }

        fpsLabel = new Label("FPS: 0", new Point(20,40), Color.GREEN, 36);
        guiLayer.addDrawable(fpsLabel);
    }

    public void addBodySegment() {
        Sprite last = centipede.get(centipede.size() - 1);
        Sprite nextToLast = centipede.get(centipede.size() - 2);

        SpriteMap centipedeMap = new SpriteMap(this.getResources(), R.drawable.centipede, 1280, 128, 128, 128);

        SpriteAnimated centipedeBody = new SpriteAnimated(centipedeMap, bodyAnimationSequence);

        if (nextToLast.getClass() == SpriteAnimated.class) {
            centipedeBody.setCurrentFrame((((SpriteAnimated) nextToLast).getCurrentFrame() + 1) % bodyAnimationSequence.size());
        }
        centipedeBody.setzIndex(last.getzIndex());
        centipedeBody.setRotation(last.getRotation());
        centipedeBody.moveCenter(new PointF(last.getFrame().centerX(), last.getFrame().centerY()));

        gameLayer.addDrawable(centipedeBody);

        centipede.add(centipede.size() - 1, centipedeBody);

        last.setzIndex(last.getzIndex() - 1);
    }

    public void addApple() {
        Sprite apple = new SpriteStatic(appleMap);
        apple.setzIndex(-1);
        gameLayer.addDrawable(apple);
        apple.moveCenter(new PointF(1800 * (float)Math.random(), 900 * (float)Math.random()));

    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();

            update();

            draw();

            frameTime = System.currentTimeMillis() - startFrameTime;
            if (frameTime > 0) {
                fps = 1000 / frameTime;
            }
        }

    }

    private void update() {
        fpsLabel.setText("FPS: " + fps);
        if (fps < 15) {
            fpsLabel.setTextColor(Color.RED);
        } else if (fps < 30) {
            fpsLabel.setTextColor(Color.YELLOW);
        } else {
            fpsLabel.setTextColor(Color.GREEN);
        }

        if (isMoving) {
            updateDirectionAndPosition();
        }

        timeToNextApple -= frameTime;
        if(timeToNextApple < 0) {
            addApple();
            timeToNextApple = (long)(7000 * Math.random() + 3000);
        }
    }

    public void draw() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();

            engine.draw(canvas, isMoving ? frameTime : 0);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        try {
            mainThread.join();
        } catch (InterruptedException e) {
            Log.e("GameView:pause", "Unable to thread.join(): " + e.getLocalizedMessage());
        }
    }

    public void resume() {
        playing = true;
        mainThread = new Thread(this);
        mainThread.start();
    }

    private void updateDirectionAndPosition() {
        Sprite head = centipede.get(0);
        float twopi = (float)(2 * Math.PI);

        float da = (targetAngle - head.getRotation() + twopi) % twopi;

        if (da > Math.PI)
            da -= twopi;
        else if (da < -Math.PI)
            da += twopi;

        da = Math.max(Math.min(da, (maxTurnSpeedPerSecond / fps)), -(maxTurnSpeedPerSecond / fps));

        head.setRotation(head.getRotation() + da);

        head.moveBy(new PointF((float) Math.cos(head.getRotation()) * speed / fps, (float) Math.sin(head.getRotation()) * speed / fps));

        for (int i = 1; i < centipede.size(); i++) {
            Sprite last = centipede.get(i - 1);
            Sprite body = centipede.get(i);

            float targetDistance = last.getFrame().width() / 6;

            PointF target = new PointF(
                    last.getFrame().centerX() + (float) (Math.cos(last.getRotation() + Math.PI) * targetDistance),
                    last.getFrame().centerY() + (float) (Math.sin(last.getRotation() + Math.PI) * targetDistance));

            float dx = target.x - body.getFrame().centerX();
            float dy = target.y - body.getFrame().centerY();

            float angle = (float)Math.atan2(dy, dx);

            body.setRotation(angle);

            body.moveCenter(new PointF(
                    target.x + (float) (Math.cos(body.getRotation() + Math.PI) * targetDistance ),
                    target.y + (float) (Math.sin(body.getRotation() + Math.PI) * targetDistance )
            ));
        }

    }

    public void setTargetAngle(float targetAngle) {
        this.targetAngle = targetAngle;
    }
}
