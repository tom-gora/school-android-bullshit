package com.example.galaxy_warrior;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
public class SpaceGameView extends SurfaceView implements Runnable {
    // -------------- VARS FROM PROVIDED BASE CODE ------------------------------------------------
    private final Context context;
    private Thread gameThread = null;
    private final SurfaceHolder ourHolder;
    private volatile boolean playing = true;
    private boolean paused = false;
    private Canvas canvas;
    private final Paint paint;
    private long fps;
    private long timeThisFrame;
    private final int screenX;
    private final int screenY;
    private int score = 0;
    private int lives = 3;
    private MenuOverlay menuOverlay;
    //
    // -------------- BACKGROUND ------------------------------------------------------------------
    // grabbing the background bitmap in two copies as one is animated following the other
    // to give continuity impression of moving background
    private final Bitmap backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_alt);
    private final Bitmap backgroundBitmapFollowing = BitmapFactory.decodeResource(getResources(), R.drawable.background_alt);
    // init vertical positions for backgrounds
    private float backgroundPositionY = 0;
    private float backgroundPositionY2 = -backgroundBitmap.getHeight();
    //
    // -------------- DECLARE GAME OBJECTS --------------------------------------------------------
    public PlayerView playerView;
    private List<EnemyRowView> enemyRows;
    // var that will store calculated enemy rows count based of provided total enemy count
    private double rowCount;
    //
    // ------------- INSTANTIATE COLLISION ENGINE -------------------------------------------------
    private CollisionEngine collisionEngine;
    // -------------- DECLARE BOX FOR TOUCH CONTROLS ----------------------------------------------
    private final ControlBoxView controlBox;
    private final Rect controlBoxBounds;
    //
    // -------------- VARIOUS VARS USED IN LOGIC CONTROL AND VALUE TRACKING -----------------------
    private long lastShootTime = 0;
    private boolean isTracking = false;
    private boolean isShooting = false;
    private float touchX;
    private float touchY;
    //
    // -------------- DECLARE OBJECTS TO STORE FONTS ----------------------------------------------
    Typeface font_sevenSegments;
    Typeface font_bungee;
    //
    // -------------- ADJUSTABLE CONFIGURATION VARS -----------------------------------------------
    private final float bgScrollSpeed = 5.0f;
    private final int shipSize = 150;
    private final int enemyCount = 10;
    private final int enemiesPerRow = 6;
    private final int enemyGridOffsetFromTop = 50;
    private int bulletDensityTimeOffset = 300;
    //
    // --------------------------------------------------------------------------------------------
    // HELPER VAR USED WHEN I NEED TO OUTPUT A VALUE ON SCREEN TO EASILY INSPECT
    // COMMENTED OUT MOST OF THE TIME
    //String valueLoggerForTesting;
    //
    // -------------- MAIN CODE -------------------------------------------------------------------
    // -------------- CONSTRUCTOR AND OTHER FUNCTIONS FROM PROVIDED BASE CODE ---------------------
    public SpaceGameView(Context context, int x, int y) {
        super(context);
        this.context = context;
        this.rowCount = Math.ceil((double) enemyCount / enemiesPerRow);
        this.ourHolder = getHolder();
        this.paint = new Paint();
        this.screenX = x;
        this.screenY = y;
        this.playerView = new PlayerView(context, screenX,screenY, shipSize);
        this.controlBox = new ControlBoxView(context, screenX, screenY, this);
        this.controlBoxBounds = controlBox.bounds;
        this.enemyRows = this.prepEnemyRows();
        this.collisionEngine = new CollisionEngine(enemyRows, playerView, getResources());
        this.font_sevenSegments = ResourcesCompat.getFont(context, R.font.seven_segment);
        this.font_bungee = ResourcesCompat.getFont(context, R.font.bungee_regular);
        this.menuOverlay = new MenuOverlay(context);
        initLevel(context);
    }
    private void initLevel(Context context) {
    }
    @Override
    public void run() {
        while (playing) {
            long startTime = System.currentTimeMillis();
            if (!paused) {
                update();
            }
            draw();
            long timeThis = System.currentTimeMillis() - startTime;
            if (timeThis >= 1) {
                fps = 1000 / timeThis;
            }
        }
    }
    public void update() {
        scrollBackground();
        playerView.updateBullets();
        playerView.drawBullets(canvas);
        if (collisionEngine.detectHitOnEnemy()) score ++;

    }
    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            canvas.drawBitmap(backgroundBitmap, 0, backgroundPositionY, paint);
            canvas.drawBitmap(backgroundBitmapFollowing, 0, backgroundPositionY2, paint);
            paint.setColor(Color.parseColor("#c44079"));
//            paint.setFakeBoldText(true);
            paint.setTextSize(60);
            paint.setTypeface(font_sevenSegments);
            canvas.drawText("Score: " + score, 40, screenY - 20, paint);
            canvas.drawText("Lives: " + lives, screenX - 230, screenY - 20, paint);
//            canvas.drawText("newX: " + valueLoggerForTesting, 10, 80, paint);
            playerView.draw(canvas);
            controlBox.draw(canvas);
            for (EnemyRowView enemyRowView : enemyRows) {
                enemyRowView.draw(canvas);
            }
//            menuOverlay.draw(canvas);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    //
    // -------------- ADDED FUNCTIONS USED TO UPDATE GAME STATE -----------------------------------
    // animate the background
    public void scrollBackground() {
        backgroundPositionY += bgScrollSpeed;
        backgroundPositionY2 += bgScrollSpeed;
        if (backgroundPositionY >= backgroundBitmap.getHeight()) {
            backgroundPositionY = 0;
            backgroundPositionY2 = -backgroundBitmap.getHeight();
        }
    }
    //    construct the list of rows to draw based on enemy count
    private List<EnemyRowView> prepEnemyRows() {
        List<EnemyRowView> enemyRows = new ArrayList<>();
        // this is how much each row initial Y is offset from the previous row Y
        int rowSpacing = (int) (shipSize + enemyGridOffsetFromTop);
        // var for tracking this
        int currentSpacing = enemyGridOffsetFromTop;
        // declare the row var storing row object before adding to the list
        EnemyRowView enemyRow;
        // if just one row do not bother with building complex list. init it, add it, return
        if (rowCount == 1) {
            enemyRow = new EnemyRowView(context, screenX, shipSize, enemyCount, enemyGridOffsetFromTop);
            enemyRows.add(enemyRow);
            return enemyRows;
        } else {
            // what happens if more than 1 rows present
            // iterating from 1 to make sure we are doing one less iteration, because last row is drawn outside of the loop
            for (int i = 1; i < rowCount; i++) {
                if (i == 1) {
                    // first row is offset from the top only by the offset var without thhe shipSize
                    enemyRow = new EnemyRowView(context, screenX, shipSize, enemiesPerRow, enemyGridOffsetFromTop);
                    enemyRows.add(enemyRow);
                } else {
                    // but next rows are offset by the offset var plus the shipSize
                    currentSpacing += rowSpacing;
                    enemyRow = new EnemyRowView(context, screenX, shipSize, enemiesPerRow, currentSpacing);
                    enemyRows.add(enemyRow);
                }
            }
            currentSpacing += rowSpacing;
            // last row may have a varying number of enemies calculated with mod
            if (enemyCount == 0) return enemyRows;
            int enemyCountBottomRow = enemyCount % enemiesPerRow;
            // ana a quick check  because as noticed in testing if mod comes back as 0 then
            // we crash with division by zero error down the pipeline , so if it's 0 then just add to draw a full row, end of story
            if (enemyCountBottomRow != 0) {
                enemyRow = new EnemyRowView(context, screenX, shipSize, enemyCountBottomRow, currentSpacing);
                enemyRows.add(enemyRow);
            } else {
                enemyRow = new EnemyRowView(context, screenX, shipSize, enemiesPerRow, currentSpacing);
                enemyRows.add(enemyRow);
            }
        }
        return enemyRows;
    }
    // magic that translates touch coordinates to the plaer coordinates that
    // progressively offshoot the position to the sides depending on which side from the middle
    // of the screen the touch is appied to
    private float getCalculatedProgressiveX(float touchX, float midPoint) {
        float offset;
        if (touchX < midPoint) {
            offset = midPoint - touchX;
            return touchX - (offset) + 20;
        } else if (touchX == midPoint) return touchX;
        else {
            offset = touchX - midPoint;
            return touchX + (offset / 2) + 20;
        }
    }
    // basically a small getter for an event property to reduce code duplication a bit
    private int getTouchCount(MotionEvent event) {
        if (event.getPointerCount() == 1) return 1;
        else if (event.getPointerCount() == 2) return 2;
        else return 0;
    }
    // -------------------------- TOUCH EVENTS LOGIC ----------------------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // store touch coordinates
        touchX = event.getX();
        touchY = event.getY();
        // get the middle point of the screen
        float midPoint = screenX / 2;
        // place to stroe player x position
        float playerXPos;
        // make sure only the control "panel" box can be interacted with
        if (controlBoxBounds.contains((int) touchX, (int) touchY)) {
            // and if shooting is activated call shoot method
            // adding a bullet every <offset> milliseconds
            if (isShooting) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShootTime > bulletDensityTimeOffset) {
                    playerView.shoot();
                    lastShootTime = currentTime;
                }
            }
            // switch based on type of touch event received
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // on touchdown start tracking position
                    isTracking = true;
                    // get the player's x position based on the touch coordinates but with a progressive offset to the sides
                    // this is because many people (me included) use gesture controls that system-wide mean swipe from the edge
                    // means GO BACK/CLOSE. This is why control box is narrower than screen width but dynamically translates
                    // player position with offshoot to the side so player sprite can reach the edge of the screen.
                    playerXPos = getCalculatedProgressiveX(touchX, midPoint);
                    // make it so the x position cannot be less than 0 minus half the sprite width or greater than screen width plus half the sprite width
                    float boundedX = Math.max(0 - (shipSize / 2), Math.min(playerXPos, screenX - (shipSize / 2)));
                    // pass the value to the playerView so it can update its position
                    playerView.updatePlayerX(boundedX);
                    // touch logic -> onefinger swipe only moves the player, two finger shoots
                    // enables action by simply toggling the vars based of event props
                    if (getTouchCount(event) == 2) isShooting = true;
                    else if (getTouchCount(event) == 1) isShooting = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    // almost the same logic as above but recalulated as user is swiping the finger across control box
                    if (isTracking) {
                        playerXPos = getCalculatedProgressiveX(touchX, midPoint);
                        float boundedCurrentX = Math.max(0 - (shipSize / 2), Math.min(playerXPos, screenX - (shipSize / 2)));
                        playerView.updatePlayerX(boundedCurrentX);
                        if (getTouchCount(event) == 2) isShooting = true;
                        else if (getTouchCount(event) == 1) isShooting = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // stop all actions on fingers lifted off screen
                    isTracking = false;
                    isShooting = false;
                    break;
            }
        }
        return true;
    }
}
