package se.theslof.centipede.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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


    public GameView(Context context) {
        super(context);

        holder = getHolder();
        paint = new Paint();
        bitmapCentipede = BitmapFactory.decodeResource(this.getResources(), R.drawable.centipede);
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

            // TODO: Implement drawing of our centipede

            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        // TODO: Implement
    }

    public void resume() {
        // TODO: Implement
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

        // TODO: Implement
    }
}
