package com.example.misklahr.hopper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Mikael on 2015-04-06.
 */
public class Enemy {

    private Rect rect;
    private int health;
    private int damage;

    private int width;
    private int halfEnemyWidth;
    private int height;
    private Player player;

    private Paint paint;
    private double jumpSpeed;
    private double time;
    private boolean inAir;
    private long beforeJumpTime;
    private int jumpWait, counter;


    private float refX, refY;
    private float finX, finY;
    private double alpha;
    private double dAlpha;
    double trueAlpha;


    private int level;

    Enemy(int level, int width, int height, Paint paint, Player player) {
        this.level = level;
        damage = level;
        health = level;
        this.width = width;
        this.height = height;
        int jumpTime = 2000;
        this.paint = paint;
        inAir = false;
        this.player = player;
        jumpWait = 10;
        counter = 0;

        dAlpha = Math.PI / 8;

        jumpSpeed = (double) width / (double) jumpTime;

        int scale = 8;
        halfEnemyWidth = width / (2 * scale);

        Random random = new Random();
        int seed = random.nextInt(4);


        float xPos, yPos;
        if (seed == 0) {
            xPos = refX = random.nextInt(width + 1);
            yPos = refY = 0;
        } else if (seed == 1) {
            xPos = refX = random.nextInt(width + 1);
            yPos = refY = height;
        } else if (seed == 2) {
            yPos = refY = random.nextInt(height + 1);
            xPos = refX = 0;
        } else {
            yPos = refY = random.nextInt(height + 1);
            xPos = refX = width;
        }


        alpha = Math.atan2(player.getYPos() - yPos, player.getXPos() - xPos);
        beforeJumpTime = System.currentTimeMillis();

        rect = new Rect((int) (xPos - halfEnemyWidth), (int) (yPos - halfEnemyWidth), (int) (xPos + halfEnemyWidth), (int) (yPos + halfEnemyWidth));

    }


//    public void jump() {
//
//        if (!inAir) {
//            beforeJumpTime = System.currentTimeMillis();
//            inAir = true;
//            finX = player.getXPos();
//            finY = player.getYPos();
//
//            double dist = Math.sqrt((finX - refX) * (finX - refX) + (finY - refY) * (finY - refY));
//            time = dist / jumpSpeed;
//
//        }
//
//        long currTime = System.currentTimeMillis();
//
//        float percentage = (currTime - beforeJumpTime) / (float) time;
//
//        if (percentage >= 1) {
//            refX = finX;
//            refY = finY;
//            inAir = false;
//        }
//
//        setCoords(refX + percentage * (finX - refX), refY + percentage * (finY - refY));
//
//    }

    public void jump2() {

        long currTime = System.currentTimeMillis();
        long timeDiff = currTime - beforeJumpTime;
        trueAlpha = Math.atan2(player.getYPos() - rect.exactCenterY(), player.getXPos() - rect.exactCenterX());


        if (counter == jumpWait) {

            if (rect.exactCenterX() > width || rect.exactCenterX() < 0 || rect.exactCenterY() > height || rect.exactCenterY() < 0) {
                alpha = trueAlpha;
            }
                counter = 0;

                if (alpha < -Math.PI / 2 && trueAlpha > Math.PI / 2) {
                    alpha = alpha - dAlpha;

                    if (alpha <= -Math.PI) {
                        alpha = Math.PI + alpha % Math.PI;
                    }

                } else if (alpha > Math.PI / 2 && trueAlpha < -Math.PI / 2) {
                    alpha = alpha + dAlpha;

                    if (alpha > Math.PI) {
                        alpha = -Math.PI + alpha % Math.PI;
                    }

                } else {

                    //Vrid dAplha medurs
                    if (trueAlpha >= alpha + dAlpha) {
                        alpha = alpha + dAlpha;
                    }

                    //Vrid dAplha moturs
                    if (trueAlpha <= alpha - dAlpha) {
                        alpha = alpha - dAlpha;
                    }

                }



        }

        counter++;

        setCoords(rect.exactCenterX() + (float) (timeDiff * jumpSpeed * Math.cos(alpha)), rect.exactCenterY() + (float) (timeDiff * jumpSpeed * Math.sin(alpha)));

        beforeJumpTime = System.currentTimeMillis();
    }

    private void setCoords(float x, float y) {

        rect.set((int) (x - halfEnemyWidth), (int) (y - halfEnemyWidth), (int) (x + halfEnemyWidth), (int) (y + halfEnemyWidth));

    }

    public void drawEnemy(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }

    public boolean interacts(){
        return false;
    }

}
