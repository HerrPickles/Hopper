package com.example.misklahr.hopper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Mikael Nilsson on 2015-05-03.
 */
public class Game {

    private LinkedList<Bullet> bulletList;
    private LinkedList<Enemy> enemyList;

    private long gameTime;

    private int level;
    private int width, height;

    private Player player;

    private Paint paint;

    public Game(int level) {
        this.level = level;
        bulletList = new LinkedList<>();
        enemyList = new LinkedList<>();
        player = new Player();
        paint = new Paint();
        paint.setColor(Color.WHITE);

    }

    public Bitmap initiate(int width, int height, float x, float y, Bitmap playerPic) {

        Bitmap newPlayerPic = player.initiate(width, x, y, playerPic);
        player.jump(y, x, 0);
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
            e.jump2();
        }



/* Se till att när man klarat en level, spawna inte nya xD */
        if (enemyList.isEmpty() && System.currentTimeMillis() - gameTime > 1000) {
            for (int i = 0; i < level % 5; i++) {
                enemyList.add(new Enemy(level, width, height, paint, player));
            }
        }


        player.drawPlayer(canvas, paint);

        for (Bullet b : bulletList) {
            b.drawBullet(canvas);
        }

        for (Enemy e : enemyList) {
            e.drawEnemy(canvas);
        }

        return returnInput;

    }

    public boolean getNewInput() {
        return !player.isInAir();
    }

    public boolean ongoing() {
        if (!enemyList.isEmpty()) {
            return true;
        }
        return false;
    }
}
