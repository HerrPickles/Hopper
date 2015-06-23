package com.example.misklahr.hopper.Enemies;

import android.graphics.Canvas;

import com.example.misklahr.hopper.GameObject;
import com.example.misklahr.hopper.Player;

/**
 * Created by Mikael Nilsson on 2015-06-23.
 */
public interface Enemy {


    void jump();

    void drawObject(Canvas canvas);

    boolean intersects(GameObject gameObject);

    boolean isDead();

    void damage(Player player);



}
