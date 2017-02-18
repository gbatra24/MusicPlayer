package com.example.administrator.musictest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

/**
 * Created by Gagan on 11/28/2016.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> implements FastScrollRecyclerView.SectionedAdapter{
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
        GetImageTask task = new GetImageTask(currentAlbum, holder);
        task.execute(currentAlbum.getAlbumArtCover());
        /*Drawable drawable = Drawable.createFromPath(currentAlbum.getAlbumArtCover());
        if(drawable != null ) {
            holder.albumCoverView.setImageDrawable(drawable);
        }
        else {
            holder.albumCoverView.setImageResource(R.drawable.default_cover);
        }*/
        //holder.albumCoverView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        String title = albums.get(position).getTitle();
        return String.valueOf(title.charAt(0));
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

    class GetImageTask extends AsyncTask<String, Void, Bitmap> {


        private final Album album;
        private final AlbumAdapter.MyViewHolder holder;

        public GetImageTask(Album album, AlbumAdapter.MyViewHolder holder) {
            this.album = album;
            this.holder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //download:
            Bitmap bit = null;
            if (album.getAlbumID() != null) {
                bit = BitmapFactory.decodeFile(album.getAlbumArtCover());
            }
            return bit;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null && !result.equals(holder.albumCoverView)) {
                holder.albumCoverView.setImageBitmap(result);
                holder.albumCoverView.setVisibility(View.VISIBLE);
                return;
            }
            holder.albumCoverView.setVisibility(View.VISIBLE);
            holder.albumCoverView.setImageResource(R.drawable.default_cover);

        }
    }
}
