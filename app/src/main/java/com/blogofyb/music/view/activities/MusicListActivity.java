package com.blogofyb.music.view.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.blogofyb.music.R;
import com.blogofyb.music.presenter.LocalMusicPresenter;
import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.callbacks.MusicListCallback;
import com.blogofyb.music.utils.interfaces.PlayCallback;
import com.blogofyb.music.utils.interfaces.Presenter;
import com.blogofyb.music.utils.interfaces.View;
import com.blogofyb.music.utils.music.MyMusicPlayer;
import com.blogofyb.music.view.adapters.MusicListAdapter;
import com.blogofyb.music.view.broadcast.MyBroadcastReceiver;
import com.blogofyb.music.view.connections.PlayMusicServiceConnection;
import com.blogofyb.music.view.services.PlayingService;

import java.util.List;

public class MusicListActivity extends BasedActivity implements View<List<MusicBean>>, android.view.View.OnClickListener {
    private RecyclerView mMusics;
    private Presenter<List<MusicBean>> mPresenter;
    private ConstraintLayout mConsole;
    private BroadcastReceiver mReceiver;
    private PlayCallback mCallback;
    private ImageView mPlayOrPause;
    private ImageView mNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_music_list);
        mMusics = findViewById(R.id.rv_music_list);

        mPlayOrPause = findViewById(R.id.iv_play_status);
        mPlayOrPause.setOnClickListener(this);
        mPlayOrPause.setClickable(false);

        mNext = findViewById(R.id.iv_next);
        mNext.setOnClickListener(this);
        mNext.setClickable(false);

        mPresenter = new LocalMusicPresenter();
        if (!mPresenter.isAttached()) {
            mPresenter.attached(this);
        }

        mConsole = findViewById(R.id.cl_console);
        mConsole.setOnClickListener(this);
        mConsole.setClickable(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            mPresenter.getData();
        }

        mCallback = new MusicListCallback();
        mCallback.initWidgets(getWindow().getDecorView());

        registerReceiver();
        registerCallback();
        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(PlayMusicServiceConnection.getInstance());
        mPresenter.detached();
        unregisterReceiver(mReceiver);
        MyMusicPlayer.musics = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterCallback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMusics != null) {
            registerCallback();
        }
    }

    @Override
    public void onSuccess(List<MusicBean> data) {
        MyMusicPlayer.musics = data;
        mConsole.setClickable(true);
        mPlayOrPause.setClickable(true);
        mNext.setClickable(true);
        mMusics.setLayoutManager(new LinearLayoutManager(this));
        mMusics.setAdapter(new MusicListAdapter());
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.iv_next:
                MyMusicPlayer.playNext();
                break;
            case R.id.iv_play_status:
                if (MyMusicPlayer.isPlaying()) {
                    MyMusicPlayer.pauseMusic();
                } else {
                    MyMusicPlayer.play();
                }
                break;
            case R.id.cl_console:
                Intent intent = new Intent(MusicListActivity.this, PlayActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.getData();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {}

    private void bindService() {
        Intent intent = new Intent(this, PlayingService.class);
        bindService(intent, PlayMusicServiceConnection.getInstance(), BIND_AUTO_CREATE);
    }

    private void registerCallback() {
        MyMusicPlayer.registerCallback(mCallback);
    }

    private void unregisterCallback() {
        MyMusicPlayer.unregisterCallback(mCallback);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName() + ".broadcast.play");
        filter.addAction(getPackageName() + ".broadcast.previous");
        filter.addAction(getPackageName() + ".broadcast.next");
        mReceiver = new MyBroadcastReceiver();
        registerReceiver(mReceiver, filter);
    }
}
