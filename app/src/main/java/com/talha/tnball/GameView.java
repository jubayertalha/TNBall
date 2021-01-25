package com.talha.tnball;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import static com.talha.tnball.GameActivity.mCompletionListener;
import static com.talha.tnball.GameActivity.mMediaPlayer;

/**
 * Created by HP-NPC on 23/12/2017.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    Context context;
    SurfaceHolder surfaceHolder;
    DrawingThread drawingThread;
//    boolean touchFlag = false;

    public GameView(Context context) {
        super(context);

        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        drawingThread = new DrawingThread(this,context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        drawingThread = new DrawingThread(this,context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        drawingThread = new DrawingThread(this,context);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            drawingThread.start();
        }catch (Exception e){
            drawingThread.drawingFlag = false;
            drawingThread = null;
            drawingThread = new DrawingThread(this,context);
            drawingThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawingThread.drawingFlag = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchPoint = event.getX();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                touchFlag = true;
//                while (touchFlag){
//                    if(drawingThread.displayX/2 <= touchPoint){
//                        //Left Touch
//                        drawingThread.ball.velocityX += 0.00000001;
//                    }else {
//                        drawingThread.ball.velocityX -= 0.00000001;
//                    }
//                }
                if(drawingThread.gameOver){
                    drawingThread.playAgain();
                    playSounds(R.raw.click);
                }else {
                    if(drawingThread.displayX/2 <= touchPoint){
                        //Left Touch
                        drawingThread.ball.velocityX = 4;
                    }else {
                        drawingThread.ball.velocityX = -4;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
//                touchFlag = false;
                drawingThread.ball.velocityX = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if(touchPoint == drawingThread.displayX/2){
                    drawingThread.ball.velocityX = 0;
                }
                break;

        }

        return true;
    }

    public void playSounds(int soundID){


        GameActivity.releaseMediaPlayer();

        int result = GameActivity.mAudioManager.requestAudioFocus(GameActivity.mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            mMediaPlayer = MediaPlayer.create(context, soundID);

            mMediaPlayer.start();

            mMediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    }
}
