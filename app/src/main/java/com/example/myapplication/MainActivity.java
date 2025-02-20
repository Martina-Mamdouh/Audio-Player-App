package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);





        playerDuration = findViewById(R.id.player_duration);
        playerPosition = findViewById(R.id.player_position);
        seekBar = findViewById(R.id.seek_bar);
        btRew = findViewById(R.id.bt_rew);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btFf = findViewById(R.id.bt_ff);


        mediaPlayer = MediaPlayer.create(this, R.raw.audio_file);


        int duration = mediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
        playerDuration.setText(sDuration);


        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
                handler.postDelayed(this, 500);
            }
        };


        btPlay.setOnClickListener(v -> {
            btPlay.setVisibility(View.GONE);
            btPause.setVisibility(View.VISIBLE);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            handler.postDelayed(runnable, 0);
        });


        btPause.setOnClickListener(v -> {
            btPause.setVisibility(View.GONE);
            btPlay.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
            handler.removeCallbacks(runnable);
        });


        btFf.setOnClickListener(v -> {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int duration1 = mediaPlayer.getDuration();
            if (mediaPlayer.isPlaying() && currentPosition + 5000 <= duration1) {
                currentPosition += 5000;
                mediaPlayer.seekTo(currentPosition);
                playerPosition.setText(convertFormat(currentPosition));
            }
        });


        btRew.setOnClickListener(v -> {
            int currentPosition = mediaPlayer.getCurrentPosition();
            if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                currentPosition -= 5000;
                mediaPlayer.seekTo(currentPosition);
                playerPosition.setText(convertFormat(currentPosition));
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        mediaPlayer.setOnCompletionListener(mp -> {
            btPause.setVisibility(View.GONE);
            btPlay.setVisibility(View.VISIBLE);
            mediaPlayer.seekTo(0);
        });
    }


    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }
}
