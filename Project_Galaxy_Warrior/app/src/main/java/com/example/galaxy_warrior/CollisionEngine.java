package com.example.galaxy_warrior;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class CollisionEngine {
    List<EnemyRowView> enemyRowsList;
    PlayerView player;
    List<Bullet> bulletsList;
    Resources res;
    public CollisionEngine(List<EnemyRowView> enemyRowsList, PlayerView player, Resources res) {
        this.enemyRowsList = enemyRowsList;
        this.player = player;
        this.res = res;
    }
    public boolean detectHitOnEnemy() {
        bulletsList = player.getBullets();
        Rect bulletRect;
        Rect enemyRect;
        // Using iterators to avoid concurrency issues of foreach
        // but since those are rarer yet still possible, throwing in try/catch for good measure
        Bitmap explosionSprite = BitmapFactory.decodeResource(res, R.drawable.explosion);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Iterator<Bullet> ib= bulletsList.iterator();
        Iterator<EnemyRowView> ie;
        try {
            while (ib.hasNext()) {
                Bullet bullet = ib.next();
                bulletRect = bullet.getTargetRect();
                ie = enemyRowsList.iterator();
                while (ie.hasNext()) {
                    EnemyRowView row = ie.next();
                    for (Enemy enemy : row.getEnemies()) {
                        enemyRect = enemy.getTargetRect();
                        if (bulletRect != null && enemyRect != null) {
                            if (bullet.getTargetRect().intersect(enemy.getTargetRect())) {
                                Log.d("Collision", "Collision detected!");
                                bulletsList.remove(bullet);
                                enemy.setEnemySprite(explosionSprite);
                                executor.schedule(() -> {
                                    row.getEnemies().remove(enemy);
                                }, 100, TimeUnit.MILLISECONDS);
                                return true;
                            }
                        } else return false;
                    }
                }
            }
        } catch(ConcurrentModificationException e) {
            Log.d("Collision", "Concurrent modification exception" + e.getMessage());
        }
        return false;
    }
}
