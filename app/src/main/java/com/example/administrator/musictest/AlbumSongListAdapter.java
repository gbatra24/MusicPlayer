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
 * Created by Gagan on 1/6/2017.
 */

public class AlbumSongListAdapter extends RecyclerView.Adapter<AlbumSongListAdapter.MyViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter {
    private ArrayList<Song> albumSongsList;
    private LayoutInflater songInflator;

    public AlbumSongListAdapter(ArrayList<Song> songs) {
        albumSongsList = songs;
        //songInflator = LayoutInflater.from(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.albums_song_list_item, parent, false);
        return new AlbumSongListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song currentSong = albumSongsList.get(position);
        holder.albumListSongTitle.setText(currentSong.getTitle());
        holder.albumListSongArtist.setText(currentSong.getArtist());
    }

    @Override
    public int getItemCount() {
        return albumSongsList.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        String title = albumSongsList.get(position).getTitle();
        return String.valueOf(title.charAt(0));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView albumListSongTitle, albumListSongArtist;
        //public ImageView albumCoverView;
        public MyViewHolder(View itemView) {
            super(itemView);
            albumListSongTitle = (TextView) itemView.findViewById(R.id.album_song_title);
            albumListSongArtist = (TextView) itemView.findViewById(R.id.album_song_artist);
            //albumCoverView = (ImageView) itemView.findViewById(R.id.album_list_cover_view);
        }
    }
/*
    @Override
    public int getCount() {
        return albumSongsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout albumSongLay = (LinearLayout) songInflator.inflate(R.layout.albums_song_list_item,parent,false);
        TextView albumListSongTitle = (TextView) albumSongLay.findViewById(R.id.album_song_title);
        TextView albumListSongArtist = (TextView) albumSongLay.findViewById(R.id.album_song_artist);
        Song currentSong = albumSongsList.get(position);
        albumListSongTitle.setText(currentSong.getTitle());
        albumListSongArtist.setText(currentSong.getArtist());
        albumSongLay.setTag(position);
        return albumSongLay;
    }*/
}
