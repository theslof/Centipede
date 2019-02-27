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
    Label logAngle;
    Label logDA;
    Label logTot;
    boolean isMoving = true;
    float speed = 250;
    float maxTurnSpeedPerSecond = (float)Math.PI * 1.5f;
    float targetAngle = (float)(-Math.PI / 2);

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

        SpriteLayer backgroundLayer = new SpriteLayer(0, "background");
        SpriteLayer gameLayer = new SpriteLayer(1, "game");
        SpriteLayer guiLayer = new SpriteLayer(2, "gui");

        engine.addLayer(backgroundLayer);
        engine.addLayer(gameLayer);
        engine.addLayer(guiLayer);

        centipede = new ArrayList<>();

        SpriteMap centipedeHead = new SpriteMap(this.getResources(), R.drawable.centipede_head, 64, 64, 64, 64);
        Sprite centipedeHeadSprite = new SpriteStatic(centipedeHead);
        centipedeHeadSprite.setSize(128, 128);
        centipedeHeadSprite.setzIndex(0);
        centipedeHeadSprite.setRotation(targetAngle);
        gameLayer.addDrawable(centipedeHeadSprite);
        centipede.add(centipedeHeadSprite);
        centipedeHeadSprite.moveCenter(new PointF(400, 600));

        SpriteMap centipedeMap = new SpriteMap(this.getResources(), R.drawable.centipede, 512, 64, 64, 64);
        for (int i = 1; i < 19; i++) {
            SpriteAnimated centipedeBody = new SpriteAnimated(centipedeMap, Arrays.asList(
                    new SpriteAnimationFrame(0,0),
                    new SpriteAnimationFrame(64,0),
                    new SpriteAnimationFrame(128,0),
                    new SpriteAnimationFrame(192,0),
                    new SpriteAnimationFrame(256,0),
                    new SpriteAnimationFrame(320,0),
                    new SpriteAnimationFrame(384,0),
                    new SpriteAnimationFrame(448,0)
            ));
            centipedeBody.setCurrentFrame(i % centipedeBody.getAnimationSequence().size());
            centipedeBody.setzIndex(-i);
            centipedeBody.setSize(128, 128);
            centipedeBody.setRotation(targetAngle);
            centipedeBody.moveCenter(new PointF(400, 600 + centipedeBody.getFrame().height() / 3 * i));

            gameLayer.addDrawable(centipedeBody);

            centipede.add(centipedeBody);
        }

        SpriteMap centipedeTail = new SpriteMap(this.getResources(), R.drawable.centipede_tail, 64, 64, 64, 64);
        Sprite centipedeTailSprite = new SpriteStatic(centipedeTail);
        centipedeTailSprite.setSize(128, 128);
        centipedeTailSprite.setzIndex(-20);
        centipedeTailSprite.setRotation(targetAngle);
        gameLayer.addDrawable(centipedeTailSprite);
        centipede.add(centipedeTailSprite);
        centipedeTailSprite.moveCenter(new PointF(400, 1450));


        fpsLabel = new Label("FPS: 0", new Point(20,40), Color.GREEN, 36);
        logAngle = new Label("", new Point(20,80), Color.BLACK, 36);
        logDA = new Label("", new Point(20,120), Color.BLACK, 36);
        logTot = new Label("", new Point(20,160), Color.BLACK, 36);
        guiLayer.addDrawable(fpsLabel);
        guiLayer.addDrawable(logAngle);
        guiLayer.addDrawable(logDA);
        guiLayer.addDrawable(logTot);
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

        logAngle.setText("Head angle: " + head.getRotation());
        logDA.setText("Target angle: " + targetAngle);

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
