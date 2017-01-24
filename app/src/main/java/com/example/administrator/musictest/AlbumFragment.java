package com.example.administrator.musictest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gagan on 11/21/2016.
 */
public class AlbumFragment extends Fragment {

    private static final int MY_READ_EXTERNAL_PERMISSION_CONSTANT = 1;
    private ArrayList<Album> albumList;
    private FastScrollRecyclerView albumView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment, container, false);
        albumView = (FastScrollRecyclerView) view.findViewById(R.id.albums_list);

        albumList = new ArrayList<Album>();
        //albumList = new ArrayList<Song>(new LinkedHashSet<Song>(albumList));
        getAlbumList();
        //albumView.setOnItemClickListener(this);
        return view;
    }

    public void getAlbumList() {

        int permissionCheckRead = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_PERMISSION_CONSTANT);
        } else {
            /*ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_PERMISSION_CONSTANT);*/
            fillAlbumAdapter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_READ_EXTERNAL_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fillAlbumAdapter();
            }
        } else {
            System.out.println("Permission is denied............................");
        }

    }

    private void fillAlbumAdapter() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;


        Cursor albumCursor = musicResolver.query(albumUri,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ARTIST},
                null, null, null);


        if (albumCursor != null && albumCursor.moveToFirst()) {

            int titleColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int idColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int artistColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int albumArtCol = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

            do {
                String albumTitle = albumCursor.getString(titleColumn);
                String albumId = albumCursor.getString(idColumn);
                String artistName = albumCursor.getString(artistColumn);
                String albumArt = albumCursor.getString(albumArtCol);
                albumList.add(new Album(albumId, albumTitle, albumTitle, artistName, albumArt));
            } while (albumCursor.moveToNext());
        }

        Collections.sort(albumList, new Comparator<Album>() {
            @Override
            public int compare(Album a, Album b) {
                return a.getAlbumName().compareTo(b.getAlbumName());
            }
        });

        // albumList = new ArrayList<Song>(new LinkedHashSet<Song>(albumList));
        AlbumAdapter albumAdt = new AlbumAdapter(albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),3);
        albumView.setLayoutManager(mLayoutManager);
        albumView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), albumView, new AlbumFragment.ClickListener() {
            @Override
            public void onClick(View childView, int Position) {
                Intent songListIntent = new Intent(getActivity(), AlbumSongListActivity.class);

                String pos = String.valueOf(Position);
                songListIntent.putExtra("pos", pos);

                String ID = albumList.get(Position).getAlbumID();
                songListIntent.putExtra("id", ID);

                String name = albumList.get(Position).getAlbumName();
                songListIntent.putExtra("name",name);

                startActivity(songListIntent);
            }
        }));
        albumView.setAdapter(albumAdt);
    }

   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent songListIntent = new Intent(this.getActivity(), AlbumSongListActivity.class);

        String pos = String.valueOf(position);
        songListIntent.putExtra("pos", pos);

        String ID = String.valueOf(id);
        songListIntent.putExtra("id", ID);

        startActivity(songListIntent);
    }*/

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, RecyclerView recyclerView, AlbumFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                    //return super.onSingleTapUp(e);
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick(View childView, int Position);
        //public void onLongClick(View childView, int Position);
    }

}
