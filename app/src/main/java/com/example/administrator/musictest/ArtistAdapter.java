package com.example.administrator.musictest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gagan on 11/21/2016.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder>{

    private ArrayList<Artist> artists;
    private LayoutInflater artistInflator;

    public ArtistAdapter(ArrayList<Artist> theArtist) {
        artists = theArtist;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView artistView;
        public MyViewHolder(View itemView) {
            super(itemView);
            artistView = (TextView) itemView.findViewById(R.id.artist_view);
        }
    }

    @Override
    public ArtistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_list_item, parent, false);
        return new ArtistAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Artist currentArtist = artists.get(position);
        holder.artistView.setText(currentArtist.getArtistName());
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

   /* @Override
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
        Song currentSong = artists.get(position);
        artistView.setText(currentSong.getArtist());
        artistLay.setTag(position);
        return artistLay;
    }*/
}
