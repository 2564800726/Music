package com.blogofyb.music.model;

import android.os.Environment;

import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.interfaces.Callback;
import com.blogofyb.music.utils.interfaces.Model;
import com.blogofyb.music.utils.interfaces.View;
import com.blogofyb.tools.thread.ThreadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScanningLocalMusic implements Model<List<MusicBean>> {
    private List<MusicBean> musics;

    public ScanningLocalMusic() {
        musics = new ArrayList<>();
    }

    @Override
    public void requestData(View<?> view, final Callback<List<MusicBean>> callback) {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                callback.onStart();
                scanningMusicFromDisk(Environment.getExternalStorageDirectory().listFiles());
                if (musics != null) {
                    Collections.sort(musics, new Comparator<MusicBean>() {
                        @Override
                        public int compare(MusicBean o1, MusicBean o2) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });
                    callback.onSuccess(musics);
                } else {
                    callback.onFailure(new Exception("No mp3 file founded"));
                }
                callback.onCompleted();
            }
        });


    }

    private void scanningMusicFromDisk(File[] files) {
        if (files == null) {
            return;
        }
        for (File fileItem : files) {
            if (fileItem.getName().endsWith(".mp3")) {
                addMusicBean(fileItem, ".mp3");
            } else if (fileItem.getName().endsWith(".flac")) {
                addMusicBean(fileItem, ".flac");
            }
            scanningMusicFromDisk(fileItem.listFiles());
        }
    }

    private void addMusicBean(File file, String ends) {
        MusicBean music = new MusicBean();
        try {
            music.setName(file.getName().split(" - ")[1].split(ends)[0]);
            music.setSinger(file.getName().split(" - ")[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            music.setName(file.getName().split(ends)[0]);
            music.setSinger("Unknown");
        }
        music.setAbsolutePath(file.getAbsolutePath());
        musics.add(music);
    }
}
