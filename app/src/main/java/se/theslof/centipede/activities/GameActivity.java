package se.theslof.centipede.activities;

import android.app.Activity;
import android.os.Bundle;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import se.theslof.centipede.R;
import se.theslof.centipede.views.GameView;

public class GameActivity extends Activity {
    GameView gameView;
    JoystickView joystickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        joystickView = (JoystickView) findViewById(R.id.joystickView);
        gameView = (GameView) findViewById(R.id.gameView);

        int deadZone = 20;

        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if (strength >= deadZone) {
                    gameView.setTargetAngle((float)(-Math.PI / 180 * angle));
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        gameView.resume();
    }
}
