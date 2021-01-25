package com.talha.tnball;

import android.content.SearchRecentSuggestionsProvider;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by HP-NPC on 24/12/2017.
 */

public class AnimationThread extends Thread {
    public static boolean animationFlag = true;

    private DrawingThread drawingThread;

    float gravityX = 9,gravityY = 9;
    float time = 0.3f;
    Ball ball;
    float lineSpeedIncrease = 1;
    float ballSpeedIncrease = 1;

    public AnimationThread(DrawingThread drawingThread){

        this.drawingThread = drawingThread;

        ball = drawingThread.ball;
    }

    @Override
    public void run() {
        animationFlag = true;
        while (animationFlag){



            ball.centerX += (ball.velocityX*time)*10*ballSpeedIncrease;//+0.5*gravityX*time*time;
            //ball.velocityX += gravityX*time;


            for(int i = 0;i<drawingThread.lines.size();i++){
                Line line = drawingThread.lines.get(i);
               // line.centerX += 0*time+0.5*gravityX*time*time*1;
                line.centerY += (0.5*gravityY*time*time*10*0.1)*10*lineSpeedIncrease;//0*time+0.5*gravityY*time*time*1;
                float height = line.height;
                float bottom = drawingThread.displayY;
                if (line.centerY>bottom){
                    drawingThread.lines.remove(i);
                }
                if(drawingThread.overStart <= line.centerY && drawingThread.overEnd >= line.centerY){
                    if(ball.centerX < line.overLeft || ball.centerX > line.overRite){
                        animationFlag = false;
                        if(drawingThread.soundFlag){
                            drawingThread.gameView.playSounds(R.raw.game_over);
                        }
                        drawingThread.gameOver = true;
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(drawingThread.context);
                        if(sharedPreferences.getInt("bs",0)<drawingThread.score){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("bs",drawingThread.score);
                            editor.commit();
                            drawingThread.bestScore = true;
                        }
                    }
                }else if(drawingThread.overEnd < line.centerY){
                    if (line.scoreFlag){
                        line.scoreFlag = false;
                        if(drawingThread.soundFlag){
                            drawingThread.gameView.playSounds(R.raw.point1);
                        }
                        drawingThread.score += 1;
                    }
                }
            }

            if(0 > ball.centerX){
                ball.centerX = 0;
            }else if (drawingThread.displayX-(drawingThread.ballBitmap.getWidth()) < ball.centerX){
                ball.centerX = drawingThread.displayX-(drawingThread.ballBitmap.getWidth());
            }


            if(drawingThread.score==15){
                drawingThread.lineColor = R.drawable.line1;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }else if(drawingThread.score==30){
                drawingThread.lineColor = R.drawable.line2;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }else if(drawingThread.score==45){
                drawingThread.lineColor = R.drawable.line3;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }else if(drawingThread.score==60){
                drawingThread.lineColor = R.drawable.line4;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }else if(drawingThread.score==75){
                drawingThread.lineColor = R.drawable.line;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }else if(drawingThread.score==90){
                drawingThread.lineColor = R.drawable.line2;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }else if(drawingThread.score==105){
                drawingThread.lineColor = R.drawable.line4;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }else if(drawingThread.score==120){
                drawingThread.lineColor = R.drawable.line1;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }else if(drawingThread.score==135){
                drawingThread.lineColor = R.drawable.line3;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }else if(drawingThread.score==150){
                drawingThread.lineColor = R.drawable.line2;
                lineSpeedIncrease += 0.0045;
                ballSpeedIncrease += 0.0015;
            }



            try {
                sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
