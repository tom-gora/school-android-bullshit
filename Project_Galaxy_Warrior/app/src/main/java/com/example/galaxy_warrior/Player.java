package com.example.galaxy_warrior;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
public class Player {
    public final Bitmap playerSprite;
    private int x;
    private final int y;
    private final int size;
    private final int offsetFromBottom = 300;
    public Player(Resources res, int screenHeight, int screenWidth, int playerSize) {
        this.playerSprite = BitmapFactory.decodeResource(res, R.drawable.player);
        int xOffset = screenWidth - playerSize;
        this.x = xOffset / 2;
        this.y = (screenHeight - playerSize) - offsetFromBottom;
        this.size = playerSize;
    }
    public void draw(Canvas canvas) {
        // Define the rectangle to draw the player sprite
        Rect spriteSourceRect = new Rect(0, 0, playerSprite.getWidth(), playerSprite.getHeight());
        Rect targetRect = new Rect(x, y, x + size, y + size);
        // Draw the player sprite on the canvas
        canvas.drawBitmap(playerSprite, spriteSourceRect, targetRect, null);
    }
    // setter for x position
    public void setX(int x) { this.x = x; }
    // getters for position
    public int getX() {
        return this.x;
    }
    public int getY() { return this.y; }
}