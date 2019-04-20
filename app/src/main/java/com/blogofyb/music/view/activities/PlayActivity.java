package com.blogofyb.music.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.blogofyb.music.R;
import com.blogofyb.music.utils.callbacks.PlayingCallback;
import com.blogofyb.music.utils.interfaces.PlayCallback;
import com.blogofyb.music.utils.music.MyMusicPlayer;

public class PlayActivity extends BasedActivity implements View.OnClickListener {
    private PlayCallback mCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_playing_music);

        ImageView mPlay = findViewById(R.id.iv_play_music);
        mPlay.setOnClickListener(this);
        ImageView previous = findViewById(R.id.iv_previous_music);
        previous.setOnClickListener(this);
        ImageView next = findViewById(R.id.iv_next_music);
        next.setOnClickListener(this);
        SeekBar mProgress = findViewById(R.id.sb_progress);
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
            }
        });

        Toolbar mToolbar = findViewById(R.id.tb_playing_music);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCallback = new PlayingCallback();
        mCallback.initWidgets(getWindow().getDecorView());
        registerCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterCallback();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_previous_music:
                MyMusicPlayer.playPrevious();
                break;
            case R.id.iv_next_music:
                MyMusicPlayer.playNext();
                break;
            case R.id.iv_play_music:
                if (MyMusicPlayer.isPlaying()) {
                    MyMusicPlayer.pauseMusic();
                } else {
                    MyMusicPlayer.play();
                }
                break;
        }
    }

    private void registerCallback() {
        MyMusicPlayer.registerCallback(mCallback);
    }

    private void unregisterCallback() {
        MyMusicPlayer.unregisterCallback(mCallback);
    }
}
