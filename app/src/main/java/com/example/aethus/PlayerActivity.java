package com.example.aethus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity
{
    public Button playBTN, nextBTN, prevBTN, fastForwardBTN, fastRewindBTN;
    TextView songNameTV, songStartTV, songEndTV;
    SeekBar seekBar;
    BarVisualizer visualizer;
    ImageView imageView;

    String songName;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateseekbar;

    //This refers to the app bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            //Handy little method I didn't have to write myself
            onBackPressed();
        }

        //lesson learned
        return super.onOptionsItemSelected(item);
    }

    //The visualizer takes a bunch of resources, so we should explode it when we don't need it
    @Override
    protected void onDestroy() {
        if (visualizer != null)
        {
            visualizer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        prevBTN = findViewById(R.id.btnprev);
        nextBTN = findViewById(R.id.btnnext);
        playBTN = findViewById(R.id.playbtn);
        fastForwardBTN = findViewById(R.id.btnff);
        fastRewindBTN = findViewById(R.id.btnfr);

        songNameTV = findViewById(R.id.txtsn);
        songStartTV = findViewById(R.id.txtsstart);
        songEndTV = findViewById(R.id.txtsstop);

        seekBar = findViewById(R.id.seekbar);
        visualizer = findViewById(R.id.blast);
        imageView = findViewById(R.id.imageview);

        //If you enter the activity with a song already playing it should stop
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        //Get the data we passed in
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos",0);
        songNameTV.setSelected(true);

        Uri uri = Uri.parse(mySongs.get(position).toString());
        this.songName = mySongs.get(position).getName();
        songNameTV.setText(this.songName);

        //Create the player
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        //A thread that is used to update the seekbar
        updateseekbar = new Thread()
        {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentposition = 0;

                while (currentposition<totalDuration)
                {
                    try {
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentposition);
                    }
                    catch (InterruptedException | IllegalStateException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        seekBar.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        songEndTV.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                songStartTV.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);



        //Start OnClickListeners--------------------------------------------------------------------
        playBTN.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying())
            {
                playBTN.setBackgroundResource(R.drawable.ic_play);
                mediaPlayer.pause();
            }
            else
            {
                playBTN.setBackgroundResource(R.drawable.ic_pause);
                mediaPlayer.start();
            }
        });

        //If the song ends, play the next one
        mediaPlayer.setOnCompletionListener(mediaPlayer -> nextBTN.performClick());

        //Weird Visualizer stuff that is required
        int audiosessionId = mediaPlayer.getAudioSessionId();
        if (audiosessionId != -1)
        {
            visualizer.setAudioSessionId(audiosessionId);
        }

        nextBTN.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position+1)%mySongs.size());
            Uri u = Uri.parse(mySongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            this.songName = mySongs.get(position).getName();
            songNameTV.setText(this.songName);
            mediaPlayer.start();
            playBTN.setBackgroundResource(R.drawable.ic_pause);
            startAnimation(imageView);
            int audiosessionId1 = mediaPlayer.getAudioSessionId();
            if (audiosessionId1 != -1)
            {
                visualizer.setAudioSessionId(audiosessionId1);
            }

        });

        prevBTN.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position-1)<0)?(mySongs.size()-1):(position-1);

            Uri u = Uri.parse(mySongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            this.songName = mySongs.get(position).getName();
            songNameTV.setText(this.songName);
            mediaPlayer.start();
            playBTN.setBackgroundResource(R.drawable.ic_pause);
            startAnimation(imageView);
            int audiosessionId12 = mediaPlayer.getAudioSessionId();
            if (audiosessionId12 != -1)
            {
                visualizer.setAudioSessionId(audiosessionId12);
            }
        });

        fastForwardBTN.setOnClickListener(view ->
        {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
            }
        });

        fastRewindBTN.setOnClickListener(view ->
        {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
            }
        });
    }//End OnClickListeners-------------------------------------------------------------------------

    public void startAnimation(View view)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration)
    {
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";

        if (sec<10)
        {
            time+="0";
        }
        time+=sec;

        return  time;
    }
}