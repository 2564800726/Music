package com.blogofyb.music.view.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.blogofyb.music.utils.callbacks.NotificationCallback;
import com.blogofyb.music.utils.interfaces.PlayCallback;
import com.blogofyb.music.utils.interfaces.PlayStyle;
import com.blogofyb.music.utils.music.MyMusicPlayer;
import com.blogofyb.music.utils.playstyles.LoopPlayStyle;

import java.io.IOException;

public class PlayingService extends Service {
    private PlayMusicBinder mBinder = new PlayMusicBinder();
    private MediaPlayer mPlayer;
    private PlayCallback mCallback;
    private PlayStyle mPlayStyle;
    public static final int NOTIFICATION_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayStyle = new LoopPlayStyle();
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayStyle.playNext(PlayStyle.FLAG_AUTO);
            }
        });
        mCallback = new NotificationCallback();
        registerCallback();
        startForeground(NOTIFICATION_ID, ((NotificationCallback) mCallback).notification());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        unregisterCallback();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void registerCallback() {
        MyMusicPlayer.registerCallback(mCallback);
    }

    private void unregisterCallback() {
        MyMusicPlayer.unregisterCallback(mCallback);
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

        public void setPlayStyle(PlayStyle style) {
            mPlayStyle = style;
        }
    }
}
