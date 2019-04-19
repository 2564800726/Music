package com.blogofyb.music.view.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.blogofyb.music.R;
import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.music.MyMusicPlayer;
import com.blogofyb.music.view.activities.MusicListActivity;

import java.io.IOException;

public class PlayingService extends Service {
    private PlayMusicBinder mBinder = new PlayMusicBinder();
    private MediaPlayer mPlayer;
    private NotificationManager mManager;
    private RemoteViews mRemoteViews;
    private Notification mNotification;

    private final int NOTIFICATION_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createRemoteViews();
        mNotification = createNotification();
        startForeground(NOTIFICATION_ID, mNotification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private Notification createNotification() {
        NotificationChannel channel = new NotificationChannel(getPackageName() + ".play", "Play Music", NotificationManager.IMPORTANCE_NONE);
        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        Intent intent = new Intent(this, MusicListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 3, intent, PendingIntent.FLAG_NO_CREATE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getPackageName() + ".play");
        return builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomBigContentView(mRemoteViews)
                .setContentIntent(pendingIntent)
                .build();
    }

    private void createRemoteViews() {
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.music_console_notification);
        Intent intentOfPlay = new Intent(getPackageName() + ".broadcast.play");
        Intent intentOfPrevious = new Intent(getPackageName() + ".broadcast.previous");
        Intent intentOfNext = new Intent(getPackageName() + ".broadcast.next");
        PendingIntent pendingIntentOfPlay = PendingIntent.getBroadcast(this, 0, intentOfPlay, 0);
        PendingIntent pendingIntentOfPrevious = PendingIntent.getBroadcast(this, 1, intentOfPrevious, 0);
        PendingIntent pendingIntentOfNext = PendingIntent.getBroadcast(this, 2, intentOfNext, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.iv_next_music_notification, pendingIntentOfNext);
        mRemoteViews.setOnClickPendingIntent(R.id.iv_play_music_notification, pendingIntentOfPlay);
        mRemoteViews.setOnClickPendingIntent(R.id.iv_previous_music_notification, pendingIntentOfPrevious);
    }

    public class PlayMusicBinder extends Binder {
        public void playMusic(String musicPath) {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(musicPath);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void playMusic() {
            if (!mPlayer.isPlaying()) {
                mPlayer.start();
            }
        }

        public void pauseMusic() {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }

        public boolean isPlaying() {
            return mPlayer.isPlaying();
        }

        public void seekToMusic(int sec) {
            mPlayer.seekTo(sec);
        }

        public int getTotalProgress() {
            return mPlayer.getDuration();
        }

        public int getCurrentProgress() {
            return mPlayer.getCurrentPosition();
        }

        public void updateNotification() {
            MusicBean music = MyMusicPlayer.musics.get(MyMusicPlayer.getCurrentIndex());
            mRemoteViews.setTextViewText(R.id.tv_song_name_notification, music.getName());
            mRemoteViews.setTextViewText(R.id.tv_singer_notification, music.getSinger());
            if (mPlayer.isPlaying()) {
                mRemoteViews.setImageViewResource(R.id.iv_play_music_notification, R.drawable.play);
            } else {
                mRemoteViews.setImageViewResource(R.id.iv_play_music_notification, R.drawable.pause);
            }
            mManager.notify(NOTIFICATION_ID, mNotification);
        }
    }
}
