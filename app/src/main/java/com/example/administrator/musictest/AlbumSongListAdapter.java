package com.example.administrator.musictest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gagan on 1/6/2017.
 */

public class AlbumSongListAdapter extends BaseAdapter{
    private ArrayList<Song> albumSongsList;
    private LayoutInflater songInflator;

    public AlbumSongListAdapter(Context c, ArrayList<Song> songs) {
        albumSongsList = songs;
        songInflator = LayoutInflater.from(c);
    }

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
    }
}
