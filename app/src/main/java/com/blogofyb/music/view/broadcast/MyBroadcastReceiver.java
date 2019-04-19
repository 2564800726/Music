package com.blogofyb.music.view.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.music.MyMusicPlayer;
import com.blogofyb.music.view.activities.MusicListActivity;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private MusicListActivity.Holder mHolder;

    public MyBroadcastReceiver(MusicListActivity.Holder mHolder) {
        this.mHolder = mHolder;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ((context.getPackageName() + ".broadcast.play").equals(intent.getAction())) {
            if (MyMusicPlayer.isPlaying()) {
                MyMusicPlayer.pauseMusic();
            } else {
                MyMusicPlayer.play();
            }
        } else if ((context.getPackageName() + ".broadcast.previous").equals(intent.getAction())) {
            MyMusicPlayer.playPrevious();
        } else if ((context.getPackageName() + ".broadcast.next").equals(intent.getAction())) {
            MyMusicPlayer.playNext();
        }
        mHolder.updateUI();
    }
}
