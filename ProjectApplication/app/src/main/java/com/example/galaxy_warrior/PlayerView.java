package com.example.galaxy_warrior;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
class PlayerView extends View {
    private int bulletSpeed = 10;
    private List<Bullet> bullets = new ArrayList<>(); // List to store bullets
    private final Player player;
    public PlayerView(Context context, int screenX, int screenY, int size) {
        super(context);
        player = new Player(getResources(), screenY, screenX, size);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw player on canvas
        player.draw(canvas);
        updateBullets();
        drawBullets(canvas);
    }
    public void updatePlayerX(float positionX) {
        player.setX(Math.round(positionX));
    }
    // Method to shoot a bullet
    public void shoot() {
        // Create a new bullet at the player's position + some magic numbers to adjust position by hand
        Bullet bullet = new Bullet(getResources(), player.getX() + 70, player.getY() + 20, bulletSpeed);
        bullets.add(bullet); // Add the bullet to the list
    }
    // Method to update the bullets' positions
    public void updateBullets() {
        // Iterate through the list of bullets and update their positions
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            // Remove the bullet if it goes off the top edge of the screen
            if (bullet.getY() <= 0 || !bullet.isAlive()) {
                bullets.remove(i);
            }
        }
    }
    // Method to draw the bullets on the canvas
    public void drawBullets(Canvas canvas) {
        // Iterate through the list of bullets and draw them on the canvas
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.draw(canvas);
            // Tossing bullets away for the garbage collector to handle from this method as well.
            // Not necessary but doing it for good measure.
            // Would not appreciate a memory clog in a real game.
            if (bullet.getY() <= 0) {
                bullets.remove(bullet);
                i--; // Adjust the loop counter to account for the removed bullet
            }
        }
    }
    public List<Bullet> getBullets() {
        return bullets;
    }
}


