package com.blogofyb.music.presenter;

import com.blogofyb.music.model.ScanningLocalMusic;
import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.interfaces.Callback;
import com.blogofyb.music.utils.interfaces.Presenter;
import com.blogofyb.music.utils.interfaces.View;
import com.blogofyb.tools.thread.ThreadManager;

import java.util.List;

public class LocalMusicPresenter implements Presenter<List<MusicBean>> {
    private View<List<MusicBean>> view;

    @Override
    public void attached(View<List<MusicBean>> view) {
        this.view = view;
    }

    @Override
    public void detached() {
        view = null;
    }

    @Override
    public boolean isAttached() {
        return view != null;
    }

    @Override
    public void getData() {
        new ScanningLocalMusic().requestData(view, new Callback<List<MusicBean>>() {
            @Override
            public void onSuccess(final List<MusicBean> data) {
                ThreadManager.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        view.onSuccess(data);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onStart() {

            }
        });
    }
}
