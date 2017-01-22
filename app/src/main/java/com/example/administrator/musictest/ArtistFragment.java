package com.example.administrator.musictest;

import android.Manifest;
import android.content.ContentResolver;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gagan on 11/21/2016.
 */
public class ArtistFragment extends Fragment {

    private static final int MY_READ_EXTERNAL_PERMISSION_CONSTANT = 1;
    private ArrayList<Artist> artistList;
    private RecyclerView artistView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_fragment, container,false);
        artistView = (RecyclerView) view.findViewById(R.id.artists_list);

        artistList = new ArrayList<Artist>();

        getSongList();
        return view;
    }

    public void getSongList() {

        int permissionCheckRead = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_PERMISSION_CONSTANT);
        }
        else {
            /*ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_PERMISSION_CONSTANT);*/
            fillSongAdapter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_READ_EXTERNAL_PERMISSION_CONSTANT){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fillSongAdapter();
            }
        }
        else {
            System.out.println("Permission is denied............................");
        }

    }

    private void fillSongAdapter() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor artistCursor = musicResolver.query(artistUri,
                new String[]{MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST},
                null,null,null);

        if(artistCursor!=null && artistCursor.moveToFirst()) {
            //int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int artistColumn = artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);

            do {
                String thisID = artistCursor.getString(idColumn);
                //String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = artistCursor.getString(artistColumn);
                artistList.add(new Artist(thisID,thisArtist));
            }while (artistCursor.moveToNext());

            Collections.sort(artistList, new Comparator<Artist>() {
                @Override
                public int compare(Artist a, Artist b) {
                    return a.getArtistName().compareTo(b.getArtistName());
                }
            });

            ArtistAdapter artistAdt = new ArtistAdapter(artistList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            artistView.setLayoutManager(mLayoutManager);

            artistView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            artistView.setAdapter(artistAdt);
        }

    }

}
