package com.example.misklahr.hopper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Bullet implements GameObject{

    private Rect rect;
    private int damage;
    private int width;
    private int bulletWidth;
    private long startTime;
    private double bulletSpeed;
    private double alpha;
    private Paint paint;

    public Bullet(float spawnX, float spawnY, float x, float y, int width, Paint paint) {
        rect = new Rect();
        damage = 1;
        int bulletTime = 500;
        this.paint = paint;
        int scale = 40;
        bulletWidth = width / scale;

        this.width = width;
        bulletSpeed = width / bulletTime;

        alpha = Math.atan2(y - spawnY, x - spawnX);

        setCoords(spawnX, spawnY);
        startTime = System.currentTimeMillis();

    }

    public void drawObject(Canvas canvas){
        canvas.drawRect(rect,paint);
    }

    public void jump() {
        long currTime = System.currentTimeMillis();
        long timeDiff = currTime - startTime;
        setCoords(rect.exactCenterX() + (float) (timeDiff * bulletSpeed * Math.cos(alpha)), rect.exactCenterY() + (float) (timeDiff * bulletSpeed * Math.sin(alpha)));
        startTime = System.currentTimeMillis();
    }


    public boolean isOffscreen(int height){
        return (rect.exactCenterX() < 0 || rect.exactCenterY() < 0 || rect.exactCenterX() > width || rect.exactCenterY() > height);
    }

    private void setCoords(float x, float y) {

        rect.set((int) (x - bulletWidth /2), (int) (y - bulletWidth /2), (int) (x + bulletWidth /2), (int) (y + bulletWidth /2));

    }

    public Point getPoint(){
        return new Point(rect.left,rect.top);
    }

    public int getObjectWidth() {
        return bulletWidth;
    }

    public int getDamage(){
        return damage;
    }
}
