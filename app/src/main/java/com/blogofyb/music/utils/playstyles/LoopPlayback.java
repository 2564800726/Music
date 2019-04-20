package com.blogofyb.music.utils.playstyles;

import com.blogofyb.music.utils.interfaces.PlayCallback;
import com.blogofyb.music.utils.interfaces.PlayStyle;
import com.blogofyb.music.utils.music.MyMusicPlayer;

public class LoopPlayback implements PlayStyle {

    @Override
    public void playNext() {
        int index = MyMusicPlayer.getCurrentIndex();
        if (MyMusicPlayer.getCurrentIndex() == MyMusicPlayer.musics.size() - 1) {
            index = 0;
        } else {
            index += 1;
        }
        MyMusicPlayer.playMusic(index);
    }

    @Override
    public void playPrevious() {
        int index = MyMusicPlayer.getCurrentIndex();
        if (MyMusicPlayer.getCurrentIndex() == 0) {
            index = MyMusicPlayer.musics.size() - 1;
        } else {
            index -= 1;
        }
        MyMusicPlayer.playMusic(index);
    }
}
