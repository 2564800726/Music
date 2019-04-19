package com.blogofyb.music.view.connections;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.blogofyb.music.view.services.PlayingService;

public class PlayMusicServiceConnection implements ServiceConnection {
    private static volatile PlayMusicServiceConnection connection;
    private PlayingService.PlayMusicBinder mBinder;

    private PlayMusicServiceConnection() {}

    public static PlayMusicServiceConnection getInstance() {
        if (connection == null) {
            synchronized (PlayMusicServiceConnection.class) {
                if (connection == null) {
                    connection = new PlayMusicServiceConnection();
                }
            }
        }
        return connection;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mBinder = (PlayingService.PlayMusicBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mBinder = null;
    }

    public void playMusic(String musicPath) {
        mBinder.playMusic(musicPath);
    }

    public void playMusic() {
        mBinder.playMusic();
    }

    public void pauseMusic() {
        mBinder.pauseMusic();
    }

    public void seekToMusic(int sec) {
        mBinder.seekToMusic(sec);
    }

    public int getTotalProgress() {
        return mBinder.getTotalProgress();
    }

    public int getCurrentProgress() {
        return mBinder.getCurrentProgress();
    }

    public boolean isPlaying() {
        return mBinder.isPlaying();
    }

    public void updateNotification() {
        mBinder.updateNotification();
    }
}
