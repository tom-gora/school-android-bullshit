package com.example.galaxy_warrior;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
//this clas just stores enemy objects in a row as a list and passes values used to draw
//sprites down to the enemy objects where things get calculated and drawn
// this is so game can independently draw rows depending on the needs and configuration at the moment
public class EnemyRowView extends View {
    private final List<Enemy> enemies;
    public EnemyRowView(Context context, int screenX, int size, int enemyCountInRow, int offsetFromTop) {
        super(context);
        this.enemies = new ArrayList<>();
        //calculate gap between enemies spawn points based on screen size and number of enemies
        int enemyGap = screenX / enemyCountInRow;
        //theres 1 less gaps than enemies  + one last enemy in a row with no gap.
        // gives total width of drawn enemy area taken within the row
        int totalRowWidth = ((enemyCountInRow - 1) * enemyGap) + size;
        //calculate offset to screen edge based on that
        int sideOffset = (screenX - totalRowWidth) / 2;
        int currentX;
        for (int i = 1; i <= enemyCountInRow; i++) {
            if (i == 1) {
                // first enemy is always offset by calculated equal offset
                //quick and dirty because passingh by reference
                currentX = sideOffset;
            } else {
                // other enemies are offset by calculated gap relative to previous enemy(last enemy in list so far)
                currentX = enemies.get(enemies.size() - 1).getX() + enemyGap;
            }
            enemies.add(new Enemy(getResources(), size, currentX, offsetFromTop));
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (!hasLogged) {
//            this.log();
//        }
        // Draw player on canvas
        for (Enemy e : enemies) {
            e.draw(canvas);
        }
    }
    // debug log just once from draw method
    //    boolean hasLogged = false;
    //    private void log() {
    //        Log.d("Rows", String.valueOf(rowCount));
    //        hasLogged = true;
    //    }
}
