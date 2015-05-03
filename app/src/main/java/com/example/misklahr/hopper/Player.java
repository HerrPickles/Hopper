package com.example.misklahr.hopper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Mikael on 2015-04-03.
 */
public class Player {
    private int health;
    private int score;
    private float xPos, yPos;
    private float refX, refY;
    private float rotation;
    private float jumpTime;
    private double jumpSpeed;
    private boolean inAir;
    private boolean jump;
    private Bitmap playerPic;

    private int width;
    private int halfPlayerWidth;
    private int scale;

    private long beforeJumpTime;

    Player() {
        health = 100;
        jumpTime = 1000;
        score = 0;
        inAir = false;
        scale = 8;
        jump = true;
    }


    public Bullet createBullet(float x, float y, Paint paint) {
        Bullet bullet = new Bullet(refX, refY, x, y, width, paint);
        return bullet;
    }

    public boolean interact(float x, float y) {

        rotation = (float) Math.atan2(y-refY,x-refX) * 180 / (float) Math.PI + 90;

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

    public Bitmap initiate(int width, float x, float y, Bitmap playerPic) {
        this.width = width;
        halfPlayerWidth = width / (2 * scale);
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

    public void drawPlayer(Canvas canvas, Paint paint){
        canvas.save();
        canvas.rotate(rotation,xPos,yPos);
        canvas.drawBitmap(playerPic,(xPos - halfPlayerWidth), (yPos - halfPlayerWidth), new Paint());
        canvas.restore();
    }

    public float getXPos(){
        return xPos;
    }
    public float getYPos(){
        return yPos;
    }

}
