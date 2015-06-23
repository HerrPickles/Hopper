package com.example.misklahr.hopper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.misklahr.hopper.Enemies.Enemy;
import com.example.misklahr.hopper.Enemies.Minion;

import java.util.Iterator;
import java.util.LinkedList;


public class Game {

    private LinkedList<Bullet> bulletList;
    private LinkedList<Enemy> enemyList;

    private long gameTime;

    private int level;
    private int width, height;
    private boolean levelComplete, levelLost;

    private Player player;

    private Paint paint;

    public Game(int level) {
        this.level = level;
        bulletList = new LinkedList<>();
        enemyList = new LinkedList<>();
        player = new Player();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        levelComplete = false;
        levelLost = false;
    }

    public Bitmap initiate(int width, int height, float x, float y, Bitmap playerPic) {

        Bitmap newPlayerPic = player.initiate(width, height, x, y, playerPic);
        player.jump(x, y, 0);
        this.height = height;
        this.width = width;
        gameTime = System.currentTimeMillis();

        paint.setTextSize(width / 10);
        return newPlayerPic;
    }


    public boolean animateObjects(float x, float y, boolean input, Canvas canvas) {

        boolean returnInput = input;

        if (input) {

            if (player.shootReady()) {
                bulletList.add(player.createBullet(x, y, paint));
            }

            if (player.interact(x, y)) {
                returnInput = false;
            }
        } else {
            player.jump(x, y, 0);
        }


        Iterator<Bullet> bulletIterator = bulletList.iterator();
        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            b.jump();
            if (b.isOffscreen(height)) {
                bulletIterator.remove();
            }
        }

        Iterator<Enemy> enemyIterator = enemyList.iterator();
        while (enemyIterator.hasNext()) {
            Enemy e = enemyIterator.next();

            bulletIterator = bulletList.iterator();
            while (bulletIterator.hasNext()) {

                if (e.intersects(bulletIterator.next())) {
                    bulletIterator.remove();
                    if (e.isDead()) {
                        enemyIterator.remove();
                    }


                    if (enemyList.isEmpty()) {
                        levelComplete = true;
                    }
                }

            }

            if (e.intersects(player)){
                e.damage(player);
                if (player.isDead()){
                    levelLost = true;
                }
            }

            e.jump();
        }



        if (enemyList.isEmpty() && System.currentTimeMillis() - gameTime > 1000 && !levelComplete) {
            for (int i = 0; i < level % 5; i++) {
                enemyList.add(new Minion(level, width, height, paint, player));
            }

            if (level % 5 == 0){
                enemyList.add(new Minion(level, width, height, paint, player));
            }
        }


        player.drawObject(canvas);

        for (Bullet b : bulletList) {
            b.drawObject(canvas);
        }

        for (Enemy e : enemyList) {
            e.drawObject(canvas);
        }

        return returnInput;

    }

    public boolean getNewInput() {
        return !player.isInAir();
    }

    public boolean isLevelComplete(){
        return levelComplete;
    }

    public boolean isLevelLost(){
        return levelLost;
    }
}
