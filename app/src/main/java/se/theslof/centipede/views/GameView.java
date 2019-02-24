package se.theslof.centipede.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
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
    long fps;
    private long frameTime;
    SpriteEngine engine = new SpriteEngine();
    List<Sprite> centipede;
    Label fpsLabel;
    boolean isMoving;
    float speed = 250;
    PointF targetPosition = new PointF(200,200);

    public GameView(Context context) {
        super(context);

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
        centipedeHeadSprite.setzIndex(1);
        gameLayer.addDrawable(centipedeHeadSprite);
        centipede.add(centipedeHeadSprite);
        centipedeHeadSprite.moveTo(targetPosition);

        SpriteMap centipedeMap = new SpriteMap(this.getResources(), R.drawable.centipede, 512, 64, 64, 64);
        for (int i = 0; i < 20; i++) {
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
            centipedeBody.moveTo(targetPosition);

            gameLayer.addDrawable(centipedeBody);

            centipede.add(centipedeBody);

        }

        fpsLabel = new Label("FPS: 0", new Point(20,40), Color.GREEN, 36);
        guiLayer.addDrawable(fpsLabel);
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
        float dx = targetPosition.x - centipede.get(0).getFrame().left;
        float dy = targetPosition.y - centipede.get(0).getFrame().top;

        double dist = Math.sqrt(dx * dx + dy * dy);

        isMoving = dist > 50;

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                targetPosition = new PointF(event.getX(), event.getY());
                break;
        }

        return true;
    }

    private void updateDirectionAndPosition() {
        Sprite head = centipede.get(0);

        float dx = targetPosition.x - head.getFrame().centerX();
        float dy = targetPosition.y - head.getFrame().centerY();

        double angle = Math.min(Math.atan2(dy, dx) - head.getRotation(), Math.PI / 4);

        head.setRotation(head.getRotation() + angle);

        head.moveBy(new PointF((float) Math.cos(head.getRotation()) * speed / fps, (float) Math.sin(head.getRotation()) * speed / fps));

        for (int i = 1; i < centipede.size(); i++) {
            Sprite last = centipede.get(i - 1);
            Sprite body = centipede.get(i);

            PointF target = new PointF((float) (Math.cos(last.getRotation() + Math.PI) * 16) + last.getFrame().centerX(),
                    (float) (Math.sin(last.getRotation() + Math.PI) * 16) + last.getFrame().centerY());

            dx = target.x - body.getFrame().centerX();
            dy = target.y - body.getFrame().centerY();

            angle = Math.min(Math.atan2(dy, dx) - body.getRotation(), Math.PI / 4);

            body.setRotation(body.getRotation() + angle);

            body.moveBy(new PointF((float) Math.cos(body.getRotation()) * speed / fps, (float) Math.sin(body.getRotation()) * speed / fps));
        }

    }
}
