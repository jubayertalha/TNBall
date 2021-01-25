package com.talha.tnball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView bestScore;
    Button playButton,exitButton,aboutButton;

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

        setContentView(R.layout.activity_main);

        bestScore = (TextView)findViewById(R.id.best_score);
        playButton = (Button)findViewById(R.id.play);
        exitButton = (Button)findViewById(R.id.exit);
        aboutButton = (Button)findViewById(R.id.about);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int score = sharedPreferences.getInt("bs",0);
        if (score>0){
            bestScore.setText("Best: "+score);
            bestScore.setTextColor(Color.MAGENTA);
        }
        playButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        playButton.setBackgroundResource(R.drawable.play1);
                        break;
                    case MotionEvent.ACTION_UP:
                        playButton.setBackgroundResource(R.drawable.play);
                        playSounds(R.raw.click);
                        startActivity(new Intent(MainActivity.this,GameActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });
        exitButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        exitButton.setBackgroundResource(R.drawable.exit1);
                        playSounds(R.raw.click);
                        break;
                    case MotionEvent.ACTION_UP:
                        exitButton.setBackgroundResource(R.drawable.exit);
                        releaseMediaPlayer();
                        finish();
                        break;
                }
                return true;
            }
        });
        aboutButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        aboutButton.setBackgroundResource(R.drawable.about1);
                        break;
                    case MotionEvent.ACTION_UP:
                        aboutButton.setBackgroundResource(R.drawable.about);
                        playSounds(R.raw.click);
                        startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        break;
                }
                return true;
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
    public void playSounds(int soundID){


        releaseMediaPlayer();

        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            mMediaPlayer = MediaPlayer.create(MainActivity.this, soundID);

            mMediaPlayer.start();

            mMediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    }

    @Override
    protected void onStop() {
        releaseMediaPlayer();
        super.onStop();
    }
}
