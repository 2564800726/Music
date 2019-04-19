package com.blogofyb.music.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blogofyb.music.R;
import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.music.MyMusicPlayer;
import com.blogofyb.tools.thread.ThreadManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends BasedActivity implements View.OnClickListener {
    private Timer mTimer;
    private SeekBar mProgress;
    private ImageView mPlay;
    private TextView mCurrent;
    private TextView mTotal;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_playing_music);

        mCurrent = findViewById(R.id.tv_current_progress);
        mTotal = findViewById(R.id.tv_total_progress);

        mPlay = findViewById(R.id.iv_play_music);
        mPlay.setOnClickListener(this);
        ImageView previous = findViewById(R.id.iv_previous_music);
        previous.setOnClickListener(this);
        ImageView next = findViewById(R.id.iv_next_music);
        next.setOnClickListener(this);


        mProgress = findViewById(R.id.sb_progress);
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyMusicPlayer.seekTo(seekBar.getProgress());
                updateCurrent(seekBar.getProgress());
            }
        });

        mToolbar = findViewById(R.id.tb_playing_music);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateTotal();
        updateUI();
        startCounting();
        changePlayStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_previous_music:
                mTimer.cancel();
                MyMusicPlayer.playPrevious();
                startCounting();
                updateCurrent(0);
                updateUI();
                updateTotal();
                changePlayStatus();
                break;
            case R.id.iv_next_music:
                mTimer.cancel();
                MyMusicPlayer.playNext();
                startCounting();
                updateCurrent(0);
                updateUI();
                updateTotal();
                changePlayStatus();
                break;
            case R.id.iv_play_music:
                if (MyMusicPlayer.isPlaying()) {
                    MyMusicPlayer.pauseMusic();
                } else {
                    MyMusicPlayer.play();
                    updateCurrent(MyMusicPlayer.current());
                    startCounting();
                }
                changePlayStatus();
                break;
        }
    }

    private void changePlayStatus() {
        if (MyMusicPlayer.isPlaying()) {
            mPlay.setImageResource(R.drawable.play);
        } else {
            mPlay.setImageResource(R.drawable.pause);
        }
    }

    private void startCounting() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ThreadManager.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        ThreadManager.getInstance().post(new Runnable() {
                            @Override
                            public void run() {
                                updateCurrent(MyMusicPlayer.current());
                                mProgress.setProgress(MyMusicPlayer.current());
                                if (!MyMusicPlayer.isPlaying()) {
                                    MyMusicPlayer.pauseMusic();
                                    changePlayStatus();
                                    cancel();
                                }
                            }
                        });
                    }
                });
            }
        }, new Date(System.currentTimeMillis()), 10);
    }

    private void updateCurrent(int current) {
        Date date = new Date(current);
        SimpleDateFormat format = new SimpleDateFormat("mm:ss", new Locale("Chinese"));
        mCurrent.setText(format.format(date));
    }

    private void updateTotal() {
        mProgress.setMax(MyMusicPlayer.total());
        Date date = new Date(MyMusicPlayer.total());
        SimpleDateFormat format = new SimpleDateFormat("mm:ss", new Locale("Chinese"));
        mTotal.setText(format.format(date));
    }

    private void updateUI() {
        MusicBean music = MyMusicPlayer.musics.get(MyMusicPlayer.getCurrentIndex());
        mToolbar.setTitle(music.getName());
        mToolbar.setSubtitle(music.getSinger());
    }
}
