package com.blogofyb.music.utils.music;

import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.view.connections.PlayMusicServiceConnection;

import java.util.List;

public class MyMusicPlayer {
    public static List<MusicBean> musics;
    private static PlayMusicServiceConnection connection = PlayMusicServiceConnection.getInstance();
    private static int currentIndex = 0;

    public static void playNext() {
        if (currentIndex == musics.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex += 1;
        }
        playMusic(currentIndex);
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
        if (currentIndex == 0) {
            currentIndex = musics.size() -1;
        } else {
            currentIndex -= 1;
        }
        playMusic(currentIndex);
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
}
