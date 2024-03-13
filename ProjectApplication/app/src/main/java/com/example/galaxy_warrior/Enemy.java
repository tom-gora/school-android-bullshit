package com.example.galaxy_warrior;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
public class Enemy {
    private final Bitmap enemySprite;
    private final int x;
    private final int y;
    private final int size;
    public Enemy(Resources res, int enemySize, int nextXposition, int yOffset) {
        enemySprite = BitmapFactory.decodeResource(res, R.drawable.enemy);
        // receiving next x coordinate calculated based on the previous one, from the instantiating loop
        this.x = nextXposition;
        this.y = yOffset;
        this.size = enemySize;
    }
    public void draw(Canvas canvas) {
        Rect spriteSourceRect = new Rect(0, 0, enemySprite.getWidth(), enemySprite.getHeight());
        Rect targetRect = new Rect(x, y, x + size, y + size);
        canvas.drawBitmap(enemySprite, spriteSourceRect, targetRect, null);
    }
    public int getX() {
        return x;
    }
}
