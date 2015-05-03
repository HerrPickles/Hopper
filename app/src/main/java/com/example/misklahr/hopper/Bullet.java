package com.example.misklahr.hopper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Mikael on 2015-04-05.
 */
public class Bullet {

    private Rect rect;
    private int damage;
    private int width;
    private int scale;
    private int halfBulletWidth;
    private long startTime;
    private int bulletTime;
    private double bulletSpeed;
    private double alpha;
    private Paint paint;

    Bullet(float spawnX, float spawnY, float x, float y, int width, Paint paint) {
        rect = new Rect();
        damage = 1;
        bulletTime = 500;
        this.paint = paint;
        scale = 40;
        halfBulletWidth = width / (2 * scale);

        this.width = width;
        bulletSpeed = width / bulletTime;

        alpha = Math.atan2(y - spawnY, x - spawnX);

        setCoords(spawnX, spawnY);
        startTime = System.currentTimeMillis();

    }

    public void drawBullet(Canvas canvas){
        canvas.drawRect(rect,paint);
    }

    public void jump() {
        long currTime = System.currentTimeMillis();
        long timeDiff = currTime - startTime;
        setCoords(rect.exactCenterX() + (float) (timeDiff * bulletSpeed * Math.cos(alpha)), rect.exactCenterY() + (float) (timeDiff * bulletSpeed * Math.sin(alpha)));
        startTime = System.currentTimeMillis();
    }


    public boolean isOffscreen(int height){
        if (rect.exactCenterX() < 0 || rect.exactCenterY() < 0 || rect.exactCenterX() > width || rect.exactCenterY() > height){
            return true;
        }
        return false;
    }

    private void setCoords(float x, float y) {

        rect.set((int) (x - halfBulletWidth), (int) (y - halfBulletWidth), (int) (x + halfBulletWidth), (int) (y + halfBulletWidth));

    }
}
