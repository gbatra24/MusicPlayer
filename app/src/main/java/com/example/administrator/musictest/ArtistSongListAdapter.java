package com.example.administrator.musictest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

/**
 * Created by Gagan on 1/27/2017.
 */

public class ArtistSongListAdapter extends RecyclerView.Adapter<ArtistSongListAdapter.MyViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter{
    private ArrayList<Song> artistSongsList;

    public ArtistSongListAdapter(ArrayList<Song> artistSongsList) {
        this.artistSongsList = artistSongsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artists_song_list_item, parent, false);
        return new ArtistSongListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song currentSong = artistSongsList.get(position);
        holder.artistListSongTitle.setText(currentSong.getTitle());
        holder.artistListSongAlbum.setText(currentSong.getAlbum());
    }

    @Override
    public int getItemCount() {
        return artistSongsList.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        String title = artistSongsList.get(position).getTitle();
        return String.valueOf(title.charAt(0));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView artistListSongTitle, artistListSongAlbum;
        public MyViewHolder(View itemView) {
            super(itemView);
            artistListSongTitle = (TextView) itemView.findViewById(R.id.artist_song_title);
            artistListSongAlbum = (TextView) itemView.findViewById(R.id.artist_song_albumname);
        }
    }
}
