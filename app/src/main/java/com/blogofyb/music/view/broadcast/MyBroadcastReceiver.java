package com.blogofyb.music.view.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.blogofyb.music.utils.music.MyMusicPlayer;

public class MyBroadcastReceiver extends BroadcastReceiver {

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
    }
}
