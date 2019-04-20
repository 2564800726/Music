package com.blogofyb.music.utils.playstyles;

import com.blogofyb.music.utils.interfaces.PlayStyle;
import com.blogofyb.music.utils.music.MyMusicPlayer;

public class SingleLoopPlayStyle implements PlayStyle {
    private LoopPlayStyle mLoopPlayStyle;

    public SingleLoopPlayStyle() {
        mLoopPlayStyle = new LoopPlayStyle();
    }

    @Override
    public void playNext(int flag) {
        if (flag == FLAG_USER) {
            mLoopPlayStyle.playNext(FLAG_AUTO);
        } else {
            MyMusicPlayer.playMusic(MyMusicPlayer.getCurrentIndex());
        }
    }

    @Override
    public void playPrevious(int flag) {
        mLoopPlayStyle.playPrevious(FLAG_AUTO);
    }
}
