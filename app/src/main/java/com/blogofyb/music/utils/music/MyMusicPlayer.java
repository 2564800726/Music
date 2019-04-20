package com.blogofyb.music.utils.music;

import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.interfaces.PlayCallback;
import com.blogofyb.music.utils.interfaces.PlayStyle;
import com.blogofyb.music.utils.playstyles.LoopPlayStyle;
import com.blogofyb.music.view.connections.PlayMusicServiceConnection;
import com.blogofyb.tools.thread.ThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyMusicPlayer {
    public static List<MusicBean> musics;
    private static PlayMusicServiceConnection connection = PlayMusicServiceConnection.getInstance();
    private static int currentIndex = 0;
    private static PlayStyle playStyle = new LoopPlayStyle();
    private static List<PlayCallback> callbacks = new ArrayList<>();
    private static Timer timer = new Timer();

    static {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ThreadManager.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        if (musics != null) {
                            onPlayStatusChanged();
                        }
                    }
                });
            }
        }, 0, 100);
    }

    public static void playNext() {
        playStyle.playNext(PlayStyle.FLAG_USER);
    }

    public static void pauseMusic() {
        connection.pauseMusic();
    }

    public static void playMusic(int index) {
        currentIndex = index;
        MusicBean music = musics.get(index);
        connection.playMusic(music.getAbsolutePath());
    }

    public static void play() {
        connection.playMusic();
    }

    public static int getCurrentIndex() {
        return currentIndex;
    }

    public static void playPrevious() {
        playStyle.playPrevious(PlayStyle.FLAG_USER);
    }

    public static int total() {
        return connection.getTotalProgress();
    }

    public static int current() {
        return connection.getCurrentProgress();
    }

    public static void seekTo(int sec) {
        connection.seekToMusic(sec);
    }

    public static boolean isPlaying() {
        return connection.isPlaying();
    }

    public static void changePlayStyle(PlayStyle playStyle) {
        MyMusicPlayer.playStyle = playStyle;
        connection.setPlayStyle(playStyle);
    }

    /**
     * 注册新的回调
     * @param callback  需要注册的回调
     */
    public static void registerCallback(PlayCallback callback) {
        callbacks.add(callback);
    }

    /**
     * 取消注册
     * @param callback  需要取消的回调
     */
    public static void unregisterCallback(PlayCallback callback) {
        callbacks.remove(callback);
    }

    /**
     * 播放状态改变的时候调用
     */
    private static void onPlayStatusChanged() {
        for (PlayCallback callback : callbacks) {
            callback.updateUI();
        }
    }
}
