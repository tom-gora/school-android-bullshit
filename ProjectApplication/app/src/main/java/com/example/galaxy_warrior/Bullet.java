package com.example.galaxy_warrior;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
public class Bullet {
    private final float x;
    private float y;
    private float speed; // Speed of the bullet
    private Paint paint;
    private Bitmap laserSprite;
    // Constructor
    public Bullet(Resources res, float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.paint = new Paint();
        this.laserSprite = BitmapFactory.decodeResource(res, R.drawable.laser);
    }
    // Move the bullet upwards
    public void update() {
        y -= speed;
    }
    // Draw method to draw the bullet on the canvas
    public void draw(Canvas canvas) {
        Rect spriteSourceRect = new Rect(0, 0, laserSprite.getWidth(), laserSprite.getHeight());
        Rect targetRect = new Rect(Math.round(x - 20), Math.round(y - 60), Math.round(x + 20), Math.round(y));
        canvas.drawBitmap(laserSprite, spriteSourceRect, targetRect, paint);
    }
    // Getter method to get the Y position of the bullet
    public float getY() {
        return y;
    }
}