package se.theslof.centipede.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import se.theslof.centipede.R;

public class GameView extends SurfaceView implements Runnable {
    Thread mainThread = null;
    SurfaceHolder holder;
    volatile boolean playing;
    Canvas canvas;
    Paint paint;
    long fps;
    private long frameTime;
    Bitmap bitmapCentipede;
    boolean isMoving;
    float speed = 250;
    float posx;

    private int frameWidth = 64;
    private int frameHeight = 64;
    private int frameCount = 8;
    private int currentFrame = 0;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 1000 / 60;
    private Rect frameToDraw = new Rect(
            0,
            0,
            frameWidth,
            frameHeight);
    RectF whereToDraw = new RectF(
            posx,
            0,
            posx + frameWidth,
            frameHeight);

    public GameView(Context context) {
        super(context);

        holder = getHolder();
        paint = new Paint();
        bitmapCentipede = BitmapFactory.decodeResource(this.getResources(), R.drawable.centipede);
        bitmapCentipede = Bitmap.createScaledBitmap(bitmapCentipede, frameWidth * frameCount, frameHeight, false);
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
            posx += speed / fps;
        }
    }

    public void draw() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();

            canvas.drawColor(Color.WHITE);

            paint.setColor(Color.BLACK);

            paint.setTextSize(24);

            canvas.drawText("FPS: " + fps, 20, 40, paint);

            whereToDraw.set((int)posx,
                    0,
                    (int)posx + frameWidth,
                    frameHeight);

            getCurrentFrame();

            canvas.drawBitmap(bitmapCentipede,
                    frameToDraw,
                    whereToDraw, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void getCurrentFrame(){

        long time  = System.currentTimeMillis();
        if(isMoving) {
            if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame ++;
                if (currentFrame >= frameCount) {

                    currentFrame = 0;
                }
            }
        }
        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;

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
                isMoving = true;
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
        }

        return true;
    }
}
