package com.example.misklahr.hopper;

import android.graphics.Rect;

/**
 * Created by Mikael on 2015-04-05.
 */
public class Bullet {

    private Rect rect;
    private int damage;
    private int width;
    private long startTime;
    private int bulletTime;
    private double bulletSpeed;
    private double alpha;

    Bullet() {
        rect = new Rect();
        damage = 1;
        bulletTime = 500;
    }

    public Rect getRect() {
        return rect;
    }

    public void jump() {
        long currTime = System.currentTimeMillis();
        long timeDiff = currTime - startTime;
        setCoords(rect.exactCenterX() + (float) (timeDiff * bulletSpeed * Math.cos(alpha)), rect.exactCenterY() + (float) (timeDiff * bulletSpeed * Math.sin(alpha)));
        startTime = System.currentTimeMillis();
    }

    public void initiate(float spawnX, float spawnY, float x, float y, int width) {
        this.width = width;
        bulletSpeed = width / bulletTime;

        alpha = Math.atan2(y - spawnY, x - spawnX);

        setCoords(spawnX, spawnY);
        startTime = System.currentTimeMillis();
    }

    private void setCoords(float x, float y) {

        rect.set((int) (x - width / 80), (int) (y - width / 80), (int) (x + width / 80), (int) (y + width / 80));

    }
}
