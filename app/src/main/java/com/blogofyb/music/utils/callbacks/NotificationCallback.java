package com.blogofyb.music.utils.callbacks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.blogofyb.music.R;
import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.interfaces.PlayCallback;
import com.blogofyb.music.utils.music.MyMusicPlayer;
import com.blogofyb.music.view.activities.MusicListActivity;
import com.blogofyb.music.view.application.MyApplication;
import com.blogofyb.music.view.services.PlayingService;

public class NotificationCallback implements PlayCallback {
    private RemoteViews mRemoteViews;
    private NotificationManager mManager;
    private Notification mNotification;
    private Context mContext;
    private int flag = 0;

    public NotificationCallback() {
        this.mContext = MyApplication.getContext();
        mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        createRemoteViews();
        createNotification();
    }

    @Override
    public void updateUI() {
        if (flag == 200) {
            flag = 0;
            createRemoteViews();
            createNotification();
        }
        MusicBean music = MyMusicPlayer.musics.get(MyMusicPlayer.getCurrentIndex());
        mRemoteViews.setTextViewText(R.id.tv_song_name_notification, music.getName());
        mRemoteViews.setTextViewText(R.id.tv_singer_notification, music.getSinger());
        if (MyMusicPlayer.isPlaying()) {
            mRemoteViews.setImageViewResource(R.id.iv_play_music_notification, R.drawable.play);
        } else {
            mRemoteViews.setImageViewResource(R.id.iv_play_music_notification, R.drawable.pause);
        }
        mManager.notify(PlayingService.NOTIFICATION_ID, mNotification);
        flag++;
    }

    @Override
    public void initWidgets(View view) {

    }

    private void createRemoteViews() {
        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.music_console_notification);
        Intent intentOfPlay = new Intent(mContext.getPackageName() + ".broadcast.play");
        Intent intentOfPrevious = new Intent(mContext.getPackageName() + ".broadcast.previous");
        Intent intentOfNext = new Intent(mContext.getPackageName() + ".broadcast.next");
        PendingIntent pendingIntentOfPlay = PendingIntent.getBroadcast(mContext, 0, intentOfPlay, PendingIntent.FLAG_NO_CREATE);
        PendingIntent pendingIntentOfPrevious = PendingIntent.getBroadcast(mContext, 1, intentOfPrevious, PendingIntent.FLAG_NO_CREATE);
        PendingIntent pendingIntentOfNext = PendingIntent.getBroadcast(mContext, 2, intentOfNext, PendingIntent.FLAG_NO_CREATE);
        mRemoteViews.setOnClickPendingIntent(R.id.iv_next_music_notification, pendingIntentOfNext);
        mRemoteViews.setOnClickPendingIntent(R.id.iv_play_music_notification, pendingIntentOfPlay);
        mRemoteViews.setOnClickPendingIntent(R.id.iv_previous_music_notification, pendingIntentOfPrevious);
    }

    private void createNotification() {
        Intent intent = new Intent(mContext, MusicListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 3, intent, PendingIntent.FLAG_NO_CREATE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getPackageName() + ".play");
        mNotification =  builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomBigContentView(mRemoteViews)
                .setContentIntent(pendingIntent)
                .build();
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(mContext.getPackageName() + ".play", "Play Music", NotificationManager.IMPORTANCE_NONE);
        mManager.createNotificationChannel(channel);
    }

    public Notification notification() {
        return mNotification;
    }
}
