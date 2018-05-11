package com.example.android.spacefighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by SALONI on 17-02-2017.
 */
public class Player {

    private Bitmap bitmap;
     //coordinates
    private int x;
    private int y;
    // motion speed of character
    private int speed=0;
    ///boolean variable to track whether the ship is boosting or not
    private boolean boosting;
    //gravity value to add gravity effect to ship
    private final int GRAVITY=-10;
    // controlling y coordinate so that ship doesn't go out of the screen
    private int maxY;
    private int minY;
    private int minX;
    private  int maxX;
    //limit the bounds of sheep speed
    private final int MIN_SPEED=1;
    private final int MAX_SPEED=20;
    private Rect detectCollision;

    public Player(Context context,int screenX,int screenY){
        x=75;
        y=50;
        speed=1;
        //getting bitmap from drawable resource;
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.player);
        maxY=screenY-bitmap.getHeight();
        maxX=screenX;
        minX=0;
        minY=0;
        boosting=false; //setting the value of boosting initially to false
        detectCollision=new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
    }
    public void setBoosting()
    {
        boosting=true;
    }
    public void stopBoosting()
    {
        boosting=false;
    }
    //Method to update coordinate of character
    public void update() {
        //x++;
        if (boosting) {
            speed += 2;
        } else {
            speed -= 5;
        }

        if (speed > MAX_SPEED) {
           speed=MAX_SPEED;
        }
        if(speed <MIN_SPEED) {
            speed = MIN_SPEED;
        }

        y-=speed+GRAVITY;
        /*if(x>maxY)
            x=minX;*/
        if(y<minY) {
            y = minY;
        }
        if(y>maxY) {
            y = maxY;
        }

        //adding top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
