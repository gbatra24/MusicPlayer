package com.example.administrator.musictest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gagan on 11/17/2016.
 */

public class SongAdapter extends BaseAdapter implements Filterable{

    private ArrayList<Song> songs;
    private ArrayList<Song> filteredList;
    private LayoutInflater songInflator;
    private SongFilter songFilter;

    public SongAdapter(Context c, ArrayList<Song> theSong) {
        songs = theSong;
        filteredList = theSong;
        songInflator = LayoutInflater.from(c);
        getFilter();
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLay = (LinearLayout) songInflator.inflate(R.layout.song_list_item, parent, false);
        TextView songView = (TextView) songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);
        ImageView songCoverView = (ImageView) songLay.findViewById(R.id.song_list_album_cover);
        Song currentSong = songs.get(position);
        songView.setText(currentSong.getTitle());
        artistView.setText(currentSong.getArtist());
        Drawable drawable = Drawable.createFromPath(currentSong.getAlbumId());
        //BitmapDrawable bit = (BitmapDrawable) BitmapDrawable.createFromPath(currentSong.getAlbumId());
        songCoverView.setImageDrawable(drawable);
        songLay.setTag(position);
        return songLay;
    }

    @Override
    public Filter getFilter() {
        if(songFilter == null){
            songFilter = new SongFilter();
        }
        return songFilter;
    }

    private class SongFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<Song> tempList = new ArrayList<Song>();
                for(Song song : songs) {
                    if(song.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(song);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            }
            else {
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Song>) results.values;
            notifyDataSetChanged();
        }
    }
}
