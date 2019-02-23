package se.theslof.centipede.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;

import se.theslof.centipede.R;
import se.theslof.centipede.engine.Sprite;
import se.theslof.centipede.engine.SpriteAnimated;
import se.theslof.centipede.engine.SpriteAnimationFrame;
import se.theslof.centipede.engine.SpriteMap;
import se.theslof.centipede.engine.SpriteStatic;

public class GameView extends SurfaceView implements Runnable {
    Thread mainThread = null;
    SurfaceHolder holder;
    volatile boolean playing;
    Canvas canvas;
    Paint paint;
    long fps;
    private long frameTime;
    SpriteAnimated centipede;
    boolean isMoving;
    float speed = 250;
    double direction = 0;

    public GameView(Context context) {
        super(context);

        holder = getHolder();
        paint = new Paint();
        SpriteMap centipedeMap = new SpriteMap(this.getResources(), R.drawable.centipede, 512, 64, 64, 64);
        centipede = new SpriteAnimated(centipedeMap, Arrays.asList(
                new SpriteAnimationFrame(0,0),
                new SpriteAnimationFrame(64,0),
                new SpriteAnimationFrame(128,0),
                new SpriteAnimationFrame(192,0),
                new SpriteAnimationFrame(256,0),
                new SpriteAnimationFrame(320,0),
                new SpriteAnimationFrame(384,0),
                new SpriteAnimationFrame(448,0)
        ));
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
        if (isMoving) {
            centipede.setRotation(direction + Math.PI / 2);
            centipede.moveBy(new PointF((float) Math.cos(direction) * speed / fps, (float) Math.sin(direction) * speed / fps));
            centipede.update(frameTime);
        }
    }

    public void draw() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();

            canvas.drawColor(Color.WHITE);

            paint.setColor(Color.BLACK);

            paint.setTextSize(24);

            canvas.drawText("FPS: " + fps, 20, 40, paint);

            centipede.draw(canvas);

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
                updateDirection(event.getX(), event.getY());

                isMoving = true;
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
        }

        return true;
    }

    private void updateDirection(float x, float y) {
        float dx = x - centipede.getFrame().centerX();
        float dy = y - centipede.getFrame().centerY();

        direction = Math.atan2(dy, dx);
    }
}
