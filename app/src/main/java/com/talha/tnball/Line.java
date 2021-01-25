package com.talha.tnball;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.VelocityTracker;

/**
 * Created by HP-NPC on 23/12/2017.
 */

public class Line {

    float centerX,centerY;
    float velocityX,velocityY;
    int height, width;
    Bitmap lineBitmap,lineBitmap2;
    Paint linePaint;
    Point gravityPoint;
    int cutNum;
    float overLeft,overRite;
    boolean scoreFlag = true;

    public Line(Bitmap bitmap) {
        lineBitmap = bitmap;
        centerX = centerY = 0;
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        linePaint = new Paint();
        velocityX = velocityY = 0;
    }

    public Line(Bitmap bitmap,int cX,int cY){
        this(bitmap);
        centerX = cX;
        centerY = cY;
    }

    public Line(Bitmap bitmap,Bitmap lineBitmap2, Point point,int cutNum,float overLeft,float overRite){

        this(bitmap,point.x,point.y);
        this.cutNum = cutNum;
        this.overLeft = overLeft;
        this.overRite = overRite;
        this.lineBitmap2 = lineBitmap2;
    }

    public void setCenter(Point point){
        centerX = point.x;
        centerY = point.y;
    }

    public void setVelocity(VelocityTracker velocity){
        velocityX = velocity.getXVelocity();
        velocityY = velocity.getYVelocity();
    }
}
