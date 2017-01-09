package com.example.administrator.musictest;

import android.Manifest;
import android.content.ContentResolver;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;

/**
 * Created by Gagan on 11/21/2016.
 */
public class AlbumFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final int MY_READ_EXTERNAL_PERMISSION_CONSTANT = 1;
    private ArrayList<Song> albumList;
    private GridView albumView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment, container,false);
        albumView = (GridView) view.findViewById(R.id.albums_list);

        albumList = new ArrayList<Song>();
        //albumList = new ArrayList<Song>(new LinkedHashSet<Song>(albumList));
        getAlbumList();
        albumView.setOnItemClickListener(this);
        return view;
    }

    public void getAlbumList() {

        int permissionCheckRead = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_PERMISSION_CONSTANT);
        }
        else {
            /*ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_PERMISSION_CONSTANT);*/
            fillAlbumAdapter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_READ_EXTERNAL_PERMISSION_CONSTANT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fillAlbumAdapter();
            }
        }
        else {
            System.out.println("Permission is denied............................");
        }

    }

    private void fillAlbumAdapter() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor!=null && musicCursor.moveToFirst()) {
            //int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int albumArtColumn = musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID);


            do {
                long thisID = musicCursor.getLong(idColumn);
                //String thisTitle = musicCursor.getString(titleColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                String thisAlbumId = musicCursor.getString(albumArtColumn);

                Cursor albumCursor = musicResolver.query(albumUri,
                        new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID+ "=?",
                        new String[] {String.valueOf(thisAlbumId)},
                        null);
                if (albumCursor != null && albumCursor.moveToFirst()) {
                    thisAlbumId = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }

                albumList.add(new Song(thisID,thisAlbum,thisAlbumId));
            }while (musicCursor.moveToNext());

            albumList = new ArrayList<Song>(new LinkedHashSet<Song>(albumList));
            Collections.sort(albumList, new Comparator<Song>() {
                @Override
                public int compare(Song a, Song b) {
                    return a.getAlbum().compareTo(b.getAlbum());
                }
            });

            albumList = new ArrayList<Song>(new LinkedHashSet<Song>(albumList));
            AlbumAdapter albumAdt = new AlbumAdapter(this.getActivity(), albumList);
            albumView.setAdapter(albumAdt);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent songListIntent = new Intent(this.getActivity(),AlbumSongListActivity.class);

        String pos = String.valueOf(position);
        songListIntent.putExtra("pos",pos);

        String ID = String.valueOf(id);
        songListIntent.putExtra("id",ID);

        startActivity(songListIntent);
    }
}
