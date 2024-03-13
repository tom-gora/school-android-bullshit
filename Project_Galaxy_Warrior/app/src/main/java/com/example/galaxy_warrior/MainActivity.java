package com.example.galaxy_warrior;

import android.app.Activity;
import android.graphics.Point;

import android.os.Bundle;
import android.view.Display;
public class MainActivity extends Activity {
    SpaceGameView spaceGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        spaceGameView = new SpaceGameView(this, size.x, size.y);
        setContentView(spaceGameView);
    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();
        spaceGameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();
        // Tell the gameView pause method to execute
        spaceGameView.pause();
    }
}