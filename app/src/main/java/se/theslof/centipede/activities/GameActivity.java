package se.theslof.centipede.activities;

import android.app.Activity;
import android.os.Bundle;

import se.theslof.centipede.views.GameView;

public class GameActivity extends Activity {
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Set gameView in layout and add joystick on top, then ref. joystick in code.
        gameView = new GameView(this);
        setContentView(gameView);
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
