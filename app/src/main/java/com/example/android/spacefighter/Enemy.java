package com.example.android.spacefighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by SALONI on 17-02-2017.
 */
public class Enemy {
    //bitmap for the enemy
    //we have already pasted the bitmap in the drawable folder
    private Bitmap bitmap;
    //x and y coordinates
    private int x;
    private int y;
    //speed of enemy
    private int speed=1;

    //min and max coordinates to keep the enemy inside the screen
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;

    //to detect collision,we use a Rect object
    private Rect detectCollision;

    public Enemy(Context context,int screenX,int screenY){
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
        //initializing min and max coordinates
        maxX=screenX;
        maxY=screenY;
        minX=0;
        minY=0;
        //generating a random coordinate to add enemy
        Random generator=new Random();
        speed=generator.nextInt(6)+10;
        x=screenX;
        y=generator.nextInt(maxY)-bitmap.getHeight();

        // initializing rect object;
        detectCollision=new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());



    }
    public void update(int playerSpeed){
        x-=playerSpeed;
        x-=speed;
        //if the enemy reaches the left edge
        if(x<minX-bitmap.getWidth()){
            Random generator=new Random();
            speed=generator.nextInt(10)+10;
            x=maxX;
            y=generator.nextInt(maxY)-bitmap.getHeight();
        }
        //Adding the top, left, bottom and right to the rect object
        detectCollision.left=x;
        detectCollision.top=y;
        detectCollision.right=x+bitmap.getWidth();
        detectCollision.bottom=y+bitmap.getHeight();

    }
    public void setX(int x){
        this.x=x;
    }
    public Rect getDetectCollision(){
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



