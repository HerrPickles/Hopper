package com.example.misklahr.hopper;

import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by Mikael Nilsson on 2015-05-03.
 */
public interface GameObject {

    void drawObject(Canvas canvas);
    Point getPoint();
    int getObjectWidth();
    int getDamage();

}
