package com.example.administrator.musictest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArtistSongListActivity extends AppCompatActivity {

    private static final int MY_READ_EXTERNAL_PERMISSION_CONSTANT = 1;
    private ArrayList<Song> songsInArtist;
    private FastScrollRecyclerView songListView;
    private ArtistSongListAdapter aslAdapter;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_song_list);

        songListView = (FastScrollRecyclerView) findViewById(R.id.list_songs_in_artist);
        songsInArtist = new ArrayList<Song>();

        id = getIntent().getStringExtra("artistID");

        getSongsListOfArtist();
    }

    private void getSongsListOfArtist() {
        int permissionCheckRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_PERMISSION_CONSTANT);
        }
        else {
            /*ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_PERMISSION_CONSTANT);*/
            fillAdapter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_READ_EXTERNAL_PERMISSION_CONSTANT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fillAdapter();
            }
        }
        else {
            System.out.println("Permission is denied............................");
        }

    }

    private void fillAdapter() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null,MediaStore.Audio.Media.ARTIST_ID + "=?",
                new String[]{id}, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            do {
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

                songsInArtist.add(new Song(thisTitle, thisArtist));
                // Add the info to our array.
                /*if (this.id == thisalbumId) {
                    songsInAlbum.add(new Song(thisId, thisTitle, thisArtist));
                }*/
            }
            while (musicCursor.moveToNext());
            //musicCursor.close();

            Collections.sort(songsInArtist, new Comparator<Song>() {
                @Override
                public int compare(Song a, Song b) {
                    return a.getTitle().compareTo(b.getTitle());
                }
            });

            aslAdapter = new ArtistSongListAdapter(songsInArtist);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            songListView.setLayoutManager(mLayoutManager);
            songListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            songListView.setAdapter(aslAdapter);
        }
    }

}
