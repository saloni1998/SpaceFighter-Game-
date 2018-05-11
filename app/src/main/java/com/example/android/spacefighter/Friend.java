package com.example.android.spacefighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by SALONI on 19-02-2017.
 */
public class Friend {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed=1;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private Rect detectCollision;
    public Friend(Context context,int screenX,int screenY){
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.friend);
        maxX=screenX;
        minX=0;
        maxY=screenY;
        minY=0;
        Random generator=new Random();
        speed=generator.nextInt(6)+10;
        x=screenX;
        y=generator.nextInt(maxY)-bitmap.getHeight();
        detectCollision=new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
    }
    public void update(int playerSpeed){
        x-=playerSpeed;
        x-=speed;
        if(x<minX-bitmap.getWidth()){
            Random generator=new Random();
            speed=generator.nextInt(10)+10;
            x=maxX;
            y=generator.nextInt(maxY)-bitmap.getHeight();
            detectCollision.left=x;
            detectCollision.top= y;
            detectCollision.right= x+bitmap.getWidth();
            detectCollision.bottom= y+bitmap.getHeight();


        }
    }

    public int getX() {
        return x;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public int getY() {
        return y;
    }
}

