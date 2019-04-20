package com.blogofyb.music.utils.callbacks;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blogofyb.music.R;
import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.interfaces.PlayCallback;
import com.blogofyb.music.utils.music.MyMusicPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlayingCallback implements PlayCallback {
    private Toolbar mToolbar;
    private SeekBar mProgress;
    private ImageView mPlay;
    private TextView mCurrentProgress;
    private TextView mTotalProgress;

    @Override
    public void updateUI() {
        MusicBean music = MyMusicPlayer.musics.get(MyMusicPlayer.getCurrentIndex());
        mToolbar.setTitle(music.getName());
        mToolbar.setSubtitle(music.getSinger());
        mProgress.setMax(MyMusicPlayer.total());
        mProgress.setProgress(MyMusicPlayer.current());
        SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.CHINA);
        mCurrentProgress.setText(format.format(new Date(MyMusicPlayer.current())));
        mTotalProgress.setText(format.format(new Date(MyMusicPlayer.total())));
        if (MyMusicPlayer.isPlaying()) {
            mPlay.setImageResource(R.drawable.play);
        } else {
            mPlay.setImageResource(R.drawable.pause);
        }
    }

    @Override
    public void initWidgets(View view) {
        mToolbar = view.findViewById(R.id.tb_playing_music);
        mProgress = view.findViewById(R.id.sb_progress);
        mPlay = view.findViewById(R.id.iv_play_music);
        mCurrentProgress = view.findViewById(R.id.tv_current_progress);
        mTotalProgress = view.findViewById(R.id.tv_total_progress);
    }
}
