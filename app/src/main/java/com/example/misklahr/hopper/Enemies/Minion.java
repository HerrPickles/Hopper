package com.example.misklahr.hopper.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.misklahr.hopper.GameObject;
import com.example.misklahr.hopper.Player;

import java.util.Random;

/**
 * Created by Mikael on 2015-04-06.
 */
public class Minion implements Enemy{

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


    private float finX, finY;
    private double alpha;
    private double dAlpha;
    double trueAlpha;



    public Minion(int level, int width, int height, Paint paint, Player player) {
        damage = level / 5 + 1;
        health = level / 5 + 1;
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
            xPos = random.nextInt(width + 1);
            yPos = 0;
        } else if (seed == 1) {
            xPos = random.nextInt(width + 1);
            yPos = height;
        } else if (seed == 2) {
            yPos = random.nextInt(height + 1);
            xPos = 0;
        } else {
            yPos = random.nextInt(height + 1);
            xPos = width;
        }


        alpha = Math.atan2(player.getYPos() - yPos, player.getXPos() - xPos);
        beforeJumpTime = System.currentTimeMillis();

        rect = new Rect((int) (xPos - halfEnemyWidth), (int) (yPos - halfEnemyWidth), (int) (xPos + halfEnemyWidth), (int) (yPos + halfEnemyWidth));

    }

    public void jump() {

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

    public void drawObject(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }

    public boolean intersects(GameObject gameObject){

        int x = gameObject.getPoint().x;
        int y = gameObject.getPoint().y;
        int w = gameObject.getObjectWidth();

        if (inside(x, y)){
            health -= gameObject.getDamage();
            return true;
        }

        x += w;

        if (inside(x, y)){
            health -= gameObject.getDamage();
            return true;
        }

        y += w;

        if (inside(x, y)){
            health -= gameObject.getDamage();
            return true;
        }

        x -= w;

        if (inside(x, y)){
            health -= gameObject.getDamage();
            return true;
        }

        return false;
    }

    private boolean inside(int x, int y){

        if (x <= rect.right && x >= rect.left && y <= rect.bottom && y >= rect.top){
            return true;
        }

        return false;
    }

    public boolean isDead(){
        return health <= 0;
    }

    public void damage(Player player){
        player.damagePlayer(damage);
    }

}
