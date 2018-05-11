package com.example.android.spacefighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by SALONI on 17-02-2017.
 */
public class Boom {

    private Bitmap bitmap;
    private int x;
    private int y;

    public Boom(Context context ){
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.boom);
        //setting the coordinate outside the screen
        //so that it won't shown up in the screen
        //it will be only visible for a fraction of second
        //after collision
        x=-450;
        y=-450;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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
}
