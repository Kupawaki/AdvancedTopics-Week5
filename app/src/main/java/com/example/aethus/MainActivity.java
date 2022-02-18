package com.example.aethus;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    MediaPlayer mediaPlayer;
    private Button playBTN = findViewById(R.id.playBTN);
    private Button pauseBTN = findViewById(R.id.pauseBTN);
    private Button stopBTN = findViewById(R.id.stopBTN);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {

        super.onStop();
        stop();
    }

    private void play(View v)
    {
        if(mediaPlayer == null)
        {
            mediaPlayer = MediaPlayer.create(this, );
            mediaPlayer.setOnCompletionListener(mediaPlayer ->
            {
                stop();
            });
        }
    }

    private void pause(View v)
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.pause();
        }
    }

    private void stopBTNCall(View v)
    {
        stop();
    }

    private void stop()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "mediaPlayer is dead now", Toast.LENGTH_SHORT);
        }
    }
}