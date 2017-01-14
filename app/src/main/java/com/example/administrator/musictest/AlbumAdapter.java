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
 * Created by Gagan on 11/28/2016.
 */

public class AlbumAdapter extends BaseAdapter {
    private ArrayList<Album> albums;
    private LayoutInflater albumInflator;

    public AlbumAdapter(Context c, ArrayList<Album> theAlbum) {
        albums = theAlbum;
        albumInflator = LayoutInflater.from(c);
    }

    @Override
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
        Drawable drawable = Drawable.createFromPath(currentAlbum.getAlbumID());
        albumCoverView.setImageDrawable(drawable);
        albumCoverView.setScaleType(ImageView.ScaleType.FIT_XY);
        albumLay.setTag(position);
        return albumLay;
    }
}
