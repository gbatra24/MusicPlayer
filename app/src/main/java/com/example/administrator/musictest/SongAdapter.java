package com.example.administrator.musictest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gagan on 11/17/2016.
 */

public class SongAdapter extends BaseAdapter {

    private ArrayList<Songs> songs;
    private LayoutInflater songInflator;

    public SongAdapter(Context c, ArrayList<Songs> theSong) {
        songs = theSong;
        songInflator = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
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
        LinearLayout songLay = (LinearLayout) songInflator.inflate(R.layout.song_list_item, parent, false);
        TextView songView = (TextView) songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);
        ImageView songCoverView = (ImageView) songLay.findViewById(R.id.song_list_album_cover);
        Songs currentSong = songs.get(position);
        songView.setText(currentSong.getTitle());
        artistView.setText(currentSong.getArtist());
        Drawable drawable = Drawable.createFromPath(currentSong.getAlbumId());
        //BitmapDrawable bit = (BitmapDrawable) BitmapDrawable.createFromPath(currentSong.getAlbumId());
        songCoverView.setImageDrawable(drawable);
        songLay.setTag(position);
        return songLay;
    }
}
