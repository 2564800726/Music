package com.blogofyb.music.utils.callbacks;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogofyb.music.R;
import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.interfaces.PlayCallback;
import com.blogofyb.music.utils.music.MyMusicPlayer;

public class MusicListCallback implements PlayCallback {
    private ImageView mAlbum;
    private ImageView mPlayOrPause;
    private TextView mSongName;
    private TextView mLyric;

    @Override
    public void updateUI() {
        MusicBean music = MyMusicPlayer.musics.get(MyMusicPlayer.getCurrentIndex());
        mSongName.setText(music.getName());
        mLyric.setText(music.getSinger());
        if (MyMusicPlayer.isPlaying()) {
            mPlayOrPause.setImageResource(R.drawable.play);
        } else {
            mPlayOrPause.setImageResource(R.drawable.pause);
        }
    }

    @Override
    public void initWidgets(View view) {
        mAlbum = view.findViewById(R.id.iv_album_cover);
        mPlayOrPause = view.findViewById(R.id.iv_play_status);
        mSongName = view.findViewById(R.id.tv_song_name);
        mLyric = view.findViewById(R.id.tv_lyric);
    }
}
