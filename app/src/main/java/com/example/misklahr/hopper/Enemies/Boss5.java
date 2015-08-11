package com.example.misklahr.hopper.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.misklahr.hopper.GameObject;
import com.example.misklahr.hopper.Player;

/**
 * Created by Mikael Nilsson on 2015-06-24.
 */


public class Boss5 implements Enemy {

    public Boss5(int level, int width, int height, Paint paint, Player player){

    }


    public void jump() {

    }

    public void drawObject(Canvas canvas) {

    }

    public boolean intersects(GameObject gameObject) {
        return false;
    }

    public boolean isDead() {
        return false;
    }

    public void damage(Player player) {

    }
}
