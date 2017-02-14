package com.example.administrator.musictest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

/**
 * Created by Gagan on 11/17/2016.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder>
        implements Filterable, FastScrollRecyclerView.SectionedAdapter{

    private ArrayList<Song> songs;
    private ArrayList<Song> filteredList;
    private LayoutInflater songInflator;
    private SongFilter songFilter;

    public SongAdapter(ArrayList<Song> theSong) {
        songs = theSong;
    }

    @Override
    public Filter getFilter() {
        if(songFilter == null){
            songFilter = new SongFilter();
        }
        return songFilter;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        String title = songs.get(position).getTitle();
        return String.valueOf(title.charAt(0));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView songView,artistView;
        public ImageView songCoverView;
        public MyViewHolder(View itemView) {
            super(itemView);
            songView = (TextView) itemView.findViewById(R.id.song_title);
            artistView = (TextView) itemView.findViewById(R.id.song_artist);
            songCoverView = (ImageView) itemView.findViewById(R.id.song_list_album_cover);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song currentSong = songs.get(position);
        holder.songView.setText(currentSong.getTitle());
        holder.artistView.setText(currentSong.getArtist());
        holder.songCoverView.setVisibility(View.INVISIBLE);
        GetImageTask task = new GetImageTask(currentSong, holder);
        task.execute(currentSong.getAlbumId());

        //holder.songCoverView.setImageBitmap(bit);
    }

    @Override
    public int getItemCount() {
        return songs.size();
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
                filterResults.count = 0;
                filterResults.values = null;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Song>) results.values;
            notifyDataSetChanged();
        }
    }

    class GetImageTask extends AsyncTask<String, Void, Bitmap> {


        private final Song song;
        private final MyViewHolder holder;

        public GetImageTask(Song song, MyViewHolder holder) {
            this.song = song;
            this.holder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //download:
            Bitmap bit = null;
            if (song.getAlbumId() != null) {
                bit = BitmapFactory.decodeFile(song.getAlbumId());
            }
            return bit;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null && !result.equals(holder.songCoverView)) {
                holder.songCoverView.setImageBitmap(result);
                holder.songCoverView.setVisibility(View.VISIBLE);
                return;
            }
            holder.songCoverView.setVisibility(View.VISIBLE);
            holder.songCoverView.setImageResource(R.drawable.default_cover);

        }
    }
}
