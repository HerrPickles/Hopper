package com.example.misklahr.hopper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Player implements GameObject {
    private int fullHealth, currentHealth;
    private float xPos, yPos;
    private float refX, refY;
    private float rotation;
    private float jumpTime;
    private long invTime;
    private double jumpSpeed;
    private boolean inAir;
    private boolean invulnerable;
    private boolean jump;
    private Bitmap playerPic;


    private Rect healthbar;
    private Paint healthbarPaint;

    private int crashDamage;

    private int width, height;
    private int playerWidth;
    private int scale;

    private long beforeJumpTime;

    public Player() {
        fullHealth = currentHealth = 1;
        jumpTime = 1000;
        inAir = false;
        scale = 8;
        jump = true;
        healthbarPaint = new Paint();
        healthbarPaint.setColor(Color.GREEN);
        invulnerable = false;

        crashDamage = 0;


        healthbarPaint.setTextSize(50);

    }


    public Bullet createBullet(float x, float y, Paint paint) {
        return new Bullet(refX, refY, x, y, width, paint);
    }

    public boolean interact(float x, float y) {

        rotation = (float) Math.atan2(y - refY, x - refX) * 180 / (float) Math.PI + 90;

        if (shootReady()) {
            jump = true;
            return true;
        }


        if (!inAir && jump) {
            beforeJumpTime = System.currentTimeMillis();
            setInAir();

            refX = xPos;
            refY = yPos;

        }


        long currTime = System.currentTimeMillis();

        double dist = Math.sqrt((x - refX) * (x - refX) + (y - refY) * (y - refY));
        double time = dist / jumpSpeed;

        float percentage = (currTime - beforeJumpTime) / (float) time;

        if (percentage >= 1) {
            refX = x;
            refY = y;
            inAir = false;
            return true;
        }

        return jump(x, y, percentage);

    }

    public boolean jump(float jumpX, float jumpY, float percentage) {

        setCoords(refX + percentage * (jumpX - refX), refY + percentage * (jumpY - refY));

        return false;
    }


    public boolean isInAir() {
        return inAir;
    }


    public boolean shootReady() {
        return !inAir && !jump;
    }

    public Bitmap initiate(int width, int height, float x, float y, Bitmap playerPic) {
        this.width = width;
        this.height = height;

        healthbar = new Rect(0, 0, width, height / 30);

        playerWidth = width / scale;
        jumpSpeed = width / jumpTime;
        refX = x;
        refY = y;

        this.playerPic = Bitmap.createScaledBitmap(playerPic, width / scale, width / scale, false);
        return this.playerPic;
    }

    private void setCoords(float x, float y) {
        xPos = x;
        yPos = y;
    }

    public void setInAir() {
        inAir = true;
        jump = false;
    }

    public void drawObject(Canvas canvas) {
        long currentTime = System.currentTimeMillis();

        if ((currentTime-invTime > 333 && currentTime-invTime < 666)){
            canvas.save();
            canvas.rotate(rotation, xPos, yPos);
            canvas.drawBitmap(playerPic, (xPos - playerWidth / 2), (yPos - playerWidth / 2), healthbarPaint);
            canvas.restore();
        }

        if (currentTime-invTime > 1000){
            invulnerable = false;
            canvas.save();
            canvas.rotate(rotation, xPos, yPos);
            canvas.drawBitmap(playerPic, (xPos - playerWidth / 2), (yPos - playerWidth / 2), healthbarPaint);
            canvas.restore();
        }
        canvas.drawRect(healthbar, healthbarPaint);


    }

    public Point getPoint() {
        return new Point((int) xPos - playerWidth / 2, (int) yPos - playerWidth / 2);
    }

    public int getObjectWidth() {
        return playerWidth;
    }

    public int getDamage() {
        return crashDamage;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public void damagePlayer(int damage) {
        if (!invulnerable) {
            currentHealth -= damage;
            healthbar.set(0, 0, ((currentHealth * width) / fullHealth), height / 30);
            invulnerable = true;
            invTime = System.currentTimeMillis();
        }
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

}
