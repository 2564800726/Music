package com.blogofyb.music.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogofyb.music.R;
import com.blogofyb.music.utils.beans.MusicBean;
import com.blogofyb.music.utils.music.MyMusicPlayer;
import com.blogofyb.music.view.activities.MusicListActivity;
import com.blogofyb.music.view.connections.PlayMusicServiceConnection;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicHolder> {
    private List<MusicBean> musics;
    private MusicListActivity.Holder mHolder;

    public MusicListAdapter(MusicListActivity.Holder mHolder) {
        this.musics = MyMusicPlayer.musics;
        this.mHolder = mHolder;
    }

    @NonNull
    @Override
    public MusicListAdapter.MusicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyMusicPlayer.playMusic(MyMusicPlayer.getCurrentIndex());
        MyMusicPlayer.pauseMusic();
        mHolder.updateUI();

        mHolder.mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMusicPlayer.playNext();
                mHolder.updateUI();
            }
        });
        mHolder.mPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyMusicPlayer.isPlaying()) {
                    MyMusicPlayer.pauseMusic();
                } else {
                    MyMusicPlayer.play();
                }
                mHolder.updateUI();
            }
        });
        return new MusicHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.music_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicListAdapter.MusicHolder musicHolder, int i) {
        final MusicBean music = musics.get(i);
        musicHolder.song.setText(music.getName());
        musicHolder.singer.setText(music.getSinger());
        musicHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMusicPlayer.playMusic(musicHolder.getAdapterPosition());
                mHolder.updateUI();
            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    static class MusicHolder extends RecyclerView.ViewHolder {
        private TextView song;
        private TextView singer;

        MusicHolder(@NonNull View itemView) {
            super(itemView);
            song = itemView.findViewById(R.id.tv_song_name_item);
            singer = itemView.findViewById(R.id.tv_singer_item);
        }
    }
}
