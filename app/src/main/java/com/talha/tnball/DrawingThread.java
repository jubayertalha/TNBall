package com.talha.tnball;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by HP-NPC on 23/12/2017.
 */

public class DrawingThread extends Thread{
    private Canvas canvas;
    GameView gameView;
    Context context;
    int displayX,displayY;
    AnimationThread animationThread;

    ArrayList<Line> lines = new ArrayList<>();
    ArrayList<Integer> cutNum = new ArrayList<>();
    Random random = new Random();
    int num;
    Bitmap line1Bitmap;
    Bitmap ballBitmap;

    boolean drawingFlag = true;
    boolean gameOver = false;
    boolean bestScore = false;
    boolean soundFlag = true;

    Line line;
    Ball ball;

    float overEnd,overStart;

    int score;
    int lineColor;
    Paint paint,gameOverPaint,scorePaint,playPaint,bestPaint;


    public DrawingThread(GameView gameView, Context context){

        this.gameView = gameView;
        this.context = context;

        WindowManager windowManager =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point displayDimension = new Point();
        defaultDisplay.getSize(displayDimension);
        displayX = displayDimension.x;
        displayY = displayDimension.y;

        cutNum.add(2);
        cutNum.add(6);
        cutNum.add(10);
        cutNum.add(14);
        cutNum.add(18);

        ball = new Ball(displayX/2,0);

        line1Bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.bottom_line);
        line1Bitmap = Bitmap.createScaledBitmap(line1Bitmap,displayX,displayX/10,true);
        ballBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ball);
        ballBitmap = Bitmap.createScaledBitmap(ballBitmap,(displayX/20)*2,displayX/10,true);

        overEnd = (displayY/10)*8;
        overStart = overEnd - (line1Bitmap.getHeight()+ballBitmap.getHeight());

        score = 0;

        paint = new Paint();
        paint.setTextSize(130);
        gameOverPaint = new Paint();
        gameOverPaint.setTextSize(150);
        gameOverPaint.setColor(Color.parseColor("#FFFFFF"));
        scorePaint = new Paint();
        scorePaint.setTextSize(200);
        scorePaint.setColor(Color.parseColor("#FFFFFF"));
        playPaint = new Paint();
        playPaint.setTextSize(120);
        playPaint.setColor(Color.parseColor("#FFFFFF"));
        bestPaint = new Paint();
        bestPaint.setTextSize(70);
        bestPaint.setColor(Color.parseColor("#BBFACD"));

        lineColor = R.drawable.line;

    }

    public void addLine(){

        num = cutNum.get(random.nextInt(4));

        Bitmap lineBitmap = BitmapFactory.decodeResource(context.getResources(),lineColor);
        lineBitmap = Bitmap.createScaledBitmap(lineBitmap,(displayX/20)*num,displayX/10,true);
        Bitmap lineBitmap2 = BitmapFactory.decodeResource(context.getResources(),lineColor);
        lineBitmap2 = Bitmap.createScaledBitmap(lineBitmap2,(displayX/20)*(20-(num+4)),displayX/10,true);

        float oL = (displayX/20)*num;// + ballBitmap.getWidth() ;
        float oR = ((displayX/20)*num)+((displayX/20)*4) - ballBitmap.getWidth();

        line = new Line(lineBitmap,lineBitmap2,new Point(0,(-1*lineBitmap.getHeight())),num,oL,oR);

        lines.add(line);
    }

    public void updateCanvas(){
        canvas.drawARGB(255,255, 255, 204);
        canvas.drawBitmap(line1Bitmap,0,(displayY/10)*8,null);
        canvas.drawBitmap(ballBitmap,ball.centerX,((displayY/10)*8)-(line1Bitmap.getHeight()),null);
        for(int i=0;i<lines.size();i++){
            Line templine = lines.get(i);
            canvas.drawBitmap(templine.lineBitmap,templine.centerX,templine.centerY,templine.linePaint);
            canvas.drawBitmap(templine.lineBitmap2,((displayX/20)*templine.cutNum)+((displayX/20)*4),templine.centerY,templine.linePaint);
        }
        canvas.drawText(String.valueOf(score),20,130,paint);
        if(gameOver){
            canvas.drawARGB(200,0,0,0);
            canvas.drawText("Game Over!",(displayX/20)*3,(displayY/10)*3,gameOverPaint);
            canvas.drawText(String.valueOf(score),(displayX/20)*9,(displayY/10)*5,scorePaint);
            canvas.drawText("Play Again",(displayX/20)*5,(displayY/10)*9,playPaint);
            if(bestScore){
                canvas.drawText("New Best!",(displayX/20)*10,(displayY/10)*5.5f,bestPaint);
            }
        }
    }

    @Override
    public void run() {
        drawingFlag = true;

        animationThread = new AnimationThread(this);
        animationThread.start();
        LineAddThread lineAddThread = new LineAddThread(this,animationThread);
        lineAddThread.start();

        while(drawingFlag){
            canvas = gameView.surfaceHolder.lockCanvas();
            try {
                synchronized (gameView.surfaceHolder){
                    updateCanvas();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas!=null){
                    gameView.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            try {
                sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        animationThread.animationFlag = false;
        lineAddThread.lineAddFlag = false;
    }

    public void playAgain(){
        lines.clear();
        score = 0;
        gameOver = false;
        bestScore = false;
        animationThread = new AnimationThread(this);
        animationThread.start();
    }
}
