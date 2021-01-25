package com.talha.tnball;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    GameView gameView;
    Button stopButton,soundButton;


    public static MediaPlayer mMediaPlayer;

    public static AudioManager mAudioManager;

    public static AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    public static MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_game);

        gameView = (GameView)findViewById(R.id.gameView);
        stopButton = (Button)findViewById(R.id.stop);
        soundButton = (Button)findViewById(R.id.sound);

        stopButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        stopButton.setBackgroundResource(R.drawable.stop1);
                        gameView.playSounds(R.raw.click);
                        break;
                    case MotionEvent.ACTION_UP:
                        stopButton.setBackgroundResource(R.drawable.stop);
                        startActivity(new Intent(GameActivity.this,MainActivity.class));
                        gameView.drawingThread.animationThread.animationFlag = false;
                        gameView.drawingThread.drawingFlag = false;
                        releaseMediaPlayer();
                        finish();
                }
                return true;
            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameView.drawingThread.soundFlag){
                    gameView.drawingThread.soundFlag = false;
                    soundButton.setBackgroundResource(R.drawable.sound1);
                }else {
                    gameView.drawingThread.soundFlag = true;
                    gameView.playSounds(R.raw.click);
                    soundButton.setBackgroundResource(R.drawable.sound);
                }
            }
        });

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
    }

    public static void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GameActivity.this,MainActivity.class));
        gameView.drawingThread.animationThread.animationFlag = false;
        gameView.drawingThread.drawingFlag = false;
        finish();
    }

    @Override
    protected void onStop() {
        releaseMediaPlayer();
        super.onStop();
    }
}
