package com.example.galaxy_warrior;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
public class MenuOverlay extends SurfaceView {
    private Paint bgPaint;
    private Paint textPaint;
    private Paint borderPaint;
    public MenuOverlay(Context context) {
        super(context);
        this.bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#21416b"));
        this.textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
//        textPaint.setTextAlign(Paint.Align.LEFT);
        this.borderPaint = new Paint();
        borderPaint.setColor(Color.parseColor("#020d1b"));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        // Calculate rect coordinates centered on screen
        int rectLeft = (int) Math.round(width * 0.1);
        int rectTop = 90;
        int rectRight = width - rectLeft;
        int rectBottom = height - 500;
        // Draw background rect
        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, bgPaint);
        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, borderPaint);
// Calculate text position
        int textX = (rectLeft + rectRight) / 2; // center horizontally
        int textY = rectTop + 600; // 100px from top
        int newGameTextY = textY +400;
        int quitTextY = newGameTextY + 200;
        // Draw text
        canvas.drawText("MENU", textX-80, textY, textPaint);
        canvas.drawText("New Game", textX-80, newGameTextY, textPaint);
        canvas.drawText("Quit", textX-80, quitTextY, textPaint);
    }
}
