package com.example.administrator.musictest;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gagan on 11/28/2016.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    private ArrayList<Album> albums;
    private LayoutInflater albumInflator;

    public AlbumAdapter(ArrayList<Album> theAlbum) {
        albums = theAlbum;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Album currentAlbum = albums.get(position);
        holder.albumNameView.setText(currentAlbum.getAlbumName());
        Drawable drawable = Drawable.createFromPath(currentAlbum.getAlbumArtCover());
        if(drawable != null ) {
            holder.albumCoverView.setImageDrawable(drawable);
        }
        else {
            holder.albumCoverView.setImageResource(R.drawable.default_cover);
        }
        //holder.albumCoverView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView albumNameView;
        public ImageView albumCoverView;
        public MyViewHolder(View itemView) {
            super(itemView);
            albumNameView = (TextView) itemView.findViewById(R.id.album_view);
            albumCoverView = (ImageView) itemView.findViewById(R.id.album_list_cover_view);
        }
    }

   /* @Override
    public int getCount() {
        return albums.size();
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
        LinearLayout albumLay = (LinearLayout) albumInflator.inflate(R.layout.album_list_item, parent, false);
        TextView albumView = (TextView) albumLay.findViewById(R.id.album_view);
        ImageView albumCoverView = (ImageView) albumLay.findViewById(R.id.album_list_cover_view);
        Album currentAlbum = albums.get(position);
        albumView.setText(currentAlbum.getAlbumName());
        Drawable drawable = Drawable.createFromPath(currentAlbum.getAlbumArtCover());
        albumCoverView.setImageDrawable(drawable);
        albumCoverView.setScaleType(ImageView.ScaleType.FIT_XY);
        albumLay.setTag(position);
        return albumLay;
    }*/
}
