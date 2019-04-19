package com.blogofyb.music.view.activities;

import android.Manifest;
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
import android.widget.TextView;

import com.blogofyb.music.R;
import com.blogofyb.music.presenter.LocalMusicPresenter;
import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.interfaces.Presenter;
import com.blogofyb.music.utils.interfaces.View;
import com.blogofyb.music.utils.music.MyMusicPlayer;
import com.blogofyb.music.view.adapters.MusicListAdapter;
import com.blogofyb.music.view.broadcast.MyBroadcastReceiver;
import com.blogofyb.music.view.connections.PlayMusicServiceConnection;
import com.blogofyb.music.view.services.PlayingService;

import java.util.List;

public class MusicListActivity extends BasedActivity implements View<List<MusicBean>> {
    private RecyclerView mMusics;
    private Presenter<List<MusicBean>> mPresenter;
    private Holder mHolder;
    private ConstraintLayout mConsole;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_music_list);
        mHolder = new Holder();

        mMusics = findViewById(R.id.rv_music_list);
        mPresenter = new LocalMusicPresenter();
        if (!mPresenter.isAttached()) {
            mPresenter.attached(this);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            mPresenter.getData();
        }

        registerReceiver();

        bindService();

        mConsole = findViewById(R.id.cl_console);
        mConsole.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(MusicListActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        mConsole.setClickable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(PlayMusicServiceConnection.getInstance());
        mPresenter.detached();
    }

    private void bindService() {
        Intent intent = new Intent(this, PlayingService.class);
        bindService(intent, PlayMusicServiceConnection.getInstance(), BIND_AUTO_CREATE);
    }

    @Override
    public void onSuccess(List<MusicBean> data) {
        MyMusicPlayer.musics = data;
        mConsole.setClickable(true);
        mMusics.setLayoutManager(new LinearLayoutManager(this));
        mMusics.setAdapter(new MusicListAdapter(mHolder));
    }

    @Override
    public void onFailure() {

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
    protected void onResume() {
        super.onResume();
        if (MyMusicPlayer.musics != null) {
            mHolder.updateUI();
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName() + ".broadcast.play");
        filter.addAction(getPackageName() + ".broadcast.previous");
        filter.addAction(getPackageName() + ".broadcast.next");
        MyBroadcastReceiver receiver = new MyBroadcastReceiver(mHolder);
        registerReceiver(receiver, filter);
    }

    public class Holder {
        private ImageView mAlbum;
        public ImageView mPlayOrPause;
        public ImageView mNext;
        private TextView mSongName;
        private TextView mLyric;

        private Holder() {
            mAlbum = findViewById(R.id.iv_album_cover);
            mPlayOrPause = findViewById(R.id.iv_play_status);
            mNext = findViewById(R.id.iv_next);
            mSongName = findViewById(R.id.tv_song_name);
            mLyric = findViewById(R.id.tv_lyric);
        }

        public void updateUI() {
            MusicBean music = MyMusicPlayer.musics.get(MyMusicPlayer.getCurrentIndex());
            mSongName.setText(music.getName());
            mLyric.setText(music.getSinger());
            if (MyMusicPlayer.isPlaying()) {
                mPlayOrPause.setImageResource(R.drawable.play);
            } else {
                mPlayOrPause.setImageResource(R.drawable.pause);
            }
        }
    }
}
