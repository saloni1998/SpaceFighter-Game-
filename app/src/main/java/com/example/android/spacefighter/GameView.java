package com.example.android.spacefighter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by SALONI on 17-02-2017.
 */
//It is the actual game panel where we will play the game.
public class GameView extends SurfaceView implements Runnable {
    // boolean variable to check if the game is playing or not
    //a screenX holder
    int screenX;
    //to count the number of misses
    int countMisses;
    //indicator that the enemy has just entered the game screen
    boolean flag;
    //an indicator if the game is over
    private boolean isGameOver;
    volatile boolean playing;
    private Player player;

    //score holder
    int score;
    //the high Scores Holder
    int highScore[]=new int[4];
    //Shared Prefernces to store the High Scores
    SharedPreferences sharedPreferences;

    //these objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Friend friend;

    //adding an stars list
    private ArrayList<Star>stars=new ArrayList<Star>();
   //Adding enemies object array
    private Enemy enemies;
    //Adding 3 enemies you may increase the size
    private int enemyCount=3;

    private Thread gameThread = null;
    private Boom boom;

    //media player object to configure the background music
    static MediaPlayer gameOnsound;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;

    //context to be used in onTouchEvent to cause the activity transition from gameActivity tomainActivity
    Context context;

    public GameView(Context context,int screenX,int screenY) {
        super(context);
        this.context=context;
        player = new Player(context,screenX,screenY);
        surfaceHolder = getHolder();
        paint = new Paint();
        this.screenX=screenX;
        countMisses=0;
        isGameOver=false;
        score=0;


        gameOnsound=MediaPlayer.create(context,R.raw.gameon);
        killedEnemysound= MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound=MediaPlayer.create(context,R.raw.gameover);
        gameOnsound.start();

        sharedPreferences=context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);
        highScore[0]=sharedPreferences.getInt("score 1",0);
        highScore[1]=sharedPreferences.getInt("score 2",0);
        highScore[2]=sharedPreferences.getInt("score 3",0);
        highScore[3]=sharedPreferences.getInt("score 4",0);

        //adding 100 stars. you may increase the number
        int starNums=100;
        for(int i=0;i<starNums;i++)
        {
            Star s=new Star(screenX, screenY);
            stars.add(s);
        }
        //single enemy initialiation
        enemies = new Enemy(context,screenX,screenY);
        friend=new Friend(context, screenX, screenY);
        //initializing enemy object array
        /*enemies=new Enemy[enemyCount];
        for(int i=0;i<enemyCount;i++){
            enemies[i]=new Enemy(context, screenX, screenY);
        }*/
        boom=new Boom(context);
    }

    @Override
    public void run() {
        while (playing) {
            //to update the frame(we will update the coordinate of our characters)
            update();

            //to draw the frame(we will draw the characters to the canvas)
            draw();

            //to control(This method will control the frames per seconds drawn. Here we are calling the delay method of Thread. And this is actually making our frame rate to aroud 60fps.)
            control();
        }

    }

    private void update() {
        //updating player position;
        score++;
        player.update();

        //setting boom outside the screen
        boom.setX(-450);
        boom.setY(-450);
        //updating the stars with player speed
        for(Star s:stars){
            s.update(player.getSpeed());
        }
        if(enemies.getX()==screenX){
            flag=true;
        }
        //updating the enemy coordinate with respect to player speed
        /*for(int i=0;i<enemyCount;i++) {
            enemies[i].update(player.getSpeed());*/
        enemies.update(player.getSpeed());

            //if collision occurs with player
            if (Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())){
                //displaying boom at the location
                boom.setX(enemies.getX());
                boom.setY(enemies.getY());
                //will play a sound at the collision between player and the enemy

                killedEnemysound.start();
                //moving enemy outside the left edge
                enemies.setX(-450);
            }
        else{
                //if the enemy has just entered
                if(flag){
                    if(player.getDetectCollision().exactCenterX()>=enemies.getDetectCollision().exactCenterX()){
                        countMisses++;
                        flag=false;
                        if(countMisses==3){
                            playing=false;
                            isGameOver=true;
                            //stopping the gameOn music
                            gameOnsound.stop();
                            //play the gameOver sound
                            gameOversound.start();
                            //Assigning the scores to the highscore integer array
                            for(int i=0;i<4;i++){
                                if(highScore[i]<score){
                                    final int finalI=i;
                                    highScore[i]=score;
                                    break;
                                }
                            }
                            //storing the scores through shared Preferences
                            SharedPreferences.Editor e=sharedPreferences.edit();
                            for(int i=0;i<4;i++){
                                int j=i+1;
                                e.putInt("score"+j,highScore[i]);
                            }
                            e.apply();
                        }
                    }
                }
            }
        friend.update(player.getSpeed());
        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision())){
            boom.setX(friend.getX());
            boom.setY(friend.getY());
            playing=false;
            isGameOver=true;

            gameOnsound.stop();
            gameOversound.start();
            for(int i=0;i<4;i++){
                if(highScore[i]<score){
                    final int finalI=i;
                    highScore[i]=score;
                    break;
                }
            }
            SharedPreferences.Editor e= sharedPreferences.edit();
            for(int i=0;i<4;i++){
                int j=i+1;
                e.putInt("score"+j,highScore[i]);
            }
            e.apply();
        }
        }


    private void draw() {
        // checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing background color for canvas
            canvas.drawColor(Color.BLACK);
            //setting the paint color to white to draw the stars
            paint.setColor(Color.WHITE);
            paint.setTextSize(20);

            for(Star s:stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(),s.getY(),paint);

            }

            //drawing the score on the game screen
            paint.setTextSize(30);
            canvas.drawText("Score:"+score,100,50,paint);
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);
            canvas.drawBitmap(
                    friend.getBitmap(),
                    friend.getX(),
                    friend.getY(),
                    paint
            );
            canvas.drawBitmap(
                    enemies.getBitmap(),
                    enemies.getX(),
                    enemies.getY(),
                    paint
            );
            //drawing the enemies
            /*for(int i=0;i<enemyCount;i++){
                canvas.drawBitmap(
                        enemies[i].getBitmap(),
                        enemies[i].getX(),
                        enemies[i].getY(),
                        paint);
            }*/

            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint);

            //drawing all stars




            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                int yPos=(int)((canvas.getHeight()/2)-((paint.descent()+paint.ascent())/2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused,setting the variable to pause
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        // when the game is resumed,starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
//We will now add control to the player’s Space Jet. When the player will tap on the screen the Space Jet will boost up and after releasing the screen the Space Jet will boost down
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                //stopping the boosting when screen is release
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                //boosting the space jet when screen is pressed
                player.setBoosting();
                break;
        }
        //if the game's over, tappin on game Over screen sends you to MainActivity
        if(isGameOver){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));
            }
        }

        return true;
    }

    //stop the music on exit
    public static void stopMusic(){
        gameOnsound.stop();
    }
}