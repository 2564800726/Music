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
        updateUI(MyMusicPlayer.getCurrentIndex());
        MyMusicPlayer.pauseMusic();

        mHolder.mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMusicPlayer.playNext();
                MyMusicPlayer.pauseMusic();
                updateUI(MyMusicPlayer.getCurrentIndex());
            }
        });
        mHolder.mPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePlayStatus();
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
                MyMusicPlayer.pauseMusic();
                updateUI(musicHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    public static class MusicHolder extends RecyclerView.ViewHolder {
        private TextView song;
        private TextView singer;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            song = itemView.findViewById(R.id.tv_song_name_item);
            singer = itemView.findViewById(R.id.tv_singer_item);
        }
    }

    private void changePlayStatus() {
        if (PlayMusicServiceConnection.getInstance().isPlaying()) {
            mHolder.mPlayOrPause.setImageResource(R.drawable.pause);
            MyMusicPlayer.pauseMusic();
        } else {
            mHolder.mPlayOrPause.setImageResource(R.drawable.play);
            MyMusicPlayer.play();
        }
    }

    private void updateUI(int index) {
        MusicBean music = musics.get(index);
        mHolder.mSongName.setText(music.getName());
        mHolder.mLyric.setText(music.getSinger());
        changePlayStatus();
    }
}
