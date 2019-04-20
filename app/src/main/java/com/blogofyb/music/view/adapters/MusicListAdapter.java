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

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicHolder> {
    private List<MusicBean> musics;

    public MusicListAdapter() {
        this.musics = MyMusicPlayer.musics;
        MyMusicPlayer.playMusic(MyMusicPlayer.getCurrentIndex());
        MyMusicPlayer.pauseMusic();
    }

    @NonNull
    @Override
    public MusicListAdapter.MusicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
