package com.example.galaxy_warrior;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
public class ControlBoxView extends View {
    private final SpaceGameView spaceGameView;
    private final Paint paint = new Paint();
    private final Paint borderPaint = new Paint();
    private final int screenX;
    private final int screenY;
    public Rect bounds;
    // Width of the control box as a percentage of screen width
    int boxHeight = 280;
    int boxOffsetX = 40;
    int boxOffsetY = 80;
    public ControlBoxView(Context context, int screenX, int screenY, SpaceGameView spaceGameView) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        this.spaceGameView = spaceGameView;
        init();
    }
    private void init() {
        int[] boxDimensions = calculateBoxDimensions();
        bounds = new Rect(boxDimensions[0], boxDimensions[1], boxDimensions[2], boxDimensions[3]);
        // Semi-transparent blue (with alpha channel .8)
        paint.setColor(Color.parseColor("#8021416b"));
        paint.setStyle(Paint.Style.FILL);
        // Initialize Paint for drawing the border
        borderPaint.setColor(Color.parseColor("#020d1b"));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float borderRadius = 20.0f;
        canvas.drawRoundRect(new RectF(bounds), borderRadius, borderRadius, paint);
        canvas.drawRoundRect(new RectF(bounds), borderRadius, borderRadius, borderPaint);
    }
    // calculate box dimensions and position from the screen dimensions and preset values
    private int[] calculateBoxDimensions() {
        int boxLeft = boxOffsetX; // subtract left offset
        int boxTop = screenY - boxHeight; // place the box at the bottom of the screen minus offset
        int boxRight = screenX - boxOffsetX; // subtract right offset
        int boxBottom = boxTop + (boxHeight - boxOffsetY); // define top box coordinate
        return new int[]{boxLeft, boxTop, boxRight, boxBottom};
    }
}