package com.blogofyb.music.utils.interfaces;

import com.blogofyb.music.view.activities.MusicListActivity;

public interface PlayStyle {
    int FLAG_AUTO = 0;
    int FLAG_USER = 1;

    void playNext(int flag);

    void playPrevious(int flag);

}
