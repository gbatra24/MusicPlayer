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
 * Created by Gagan on 11/21/2016.
 */

public class ArtistAdapter extends BaseAdapter {

    private ArrayList<Songs> artists;
    private LayoutInflater artistInflator;

    public ArtistAdapter(Context c, ArrayList<Songs> theArtist) {
        artists = theArtist;
        artistInflator = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return artists.size();
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
        LinearLayout artistLay = (LinearLayout) artistInflator.inflate(R.layout.artist_list_item, parent, false);
        TextView artistView = (TextView) artistLay.findViewById(R.id.artist_view);
        Songs currentSong = artists.get(position);
        artistView.setText(currentSong.getArtist());
        artistLay.setTag(position);
        return artistLay;
    }
}
