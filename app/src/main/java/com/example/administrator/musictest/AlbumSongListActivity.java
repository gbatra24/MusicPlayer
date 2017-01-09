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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumSongListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int MY_READ_EXTERNAL_PERMISSION_CONSTANT = 1;
    private ImageView albumCoverArtImage;
    private ArrayList<Song> songsInAlbum;
    private TextView tv;
    private ListView songListView;
    private MusicService musicService;
    private Toolbar myToolbar;
    private String id;
    private AlbumSongListAdapter aslAdapter;
    private boolean musicBound = false;
    private Intent playIntent;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_song_list);

        myToolbar = (Toolbar) findViewById(R.id.app_bar_album);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        albumCoverArtImage = (ImageView) findViewById(R.id.album_cover_art);
        songListView = (ListView) findViewById(R.id.list_songs_in_album);

        songsInAlbum = new ArrayList<Song>();
        //tv = (TextView) findViewById(R.id.tv_position);
        position = Integer.parseInt(getIntent().getStringExtra("pos"));
        id = getIntent().getStringExtra("id");
        //tv.setText("" + position);
      /*  Song currentSong = musicService.getCurrentPlayingSong();
        Drawable coverDrawable =  Drawable.createFromPath(currentSong.getAlbumId());
        albumCoverArtImage.setImageDrawable(coverDrawable);*/
        getSongsListOfAlbum();
        songListView.setOnItemClickListener(this);

    }

  /*  public void onStart() {
        super.onStart();
        if(playIntent == null) {
            playIntent = new Intent(this,MusicService.class);
            this.bindService(playIntent, musicConnection,BIND_AUTO_CREATE);
            this.startService(playIntent);
        }
    }

    @Override
    public void onDestroy() {
        musicService.stopService(playIntent);
        this.unbindService(musicConnection);
        musicService=null;
        super.onDestroy();
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;

            musicService = binder.getService();
            musicService.setList(songsInAlbum);
            musicBound = true;

            Drawable drawable = Drawable.createFromPath(id);
            albumCoverArtImage.setImageDrawable(drawable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
*/

    private void getSongsListOfAlbum() {
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
                Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisalbumId = musicCursor.getString(albumId);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

                songsInAlbum.add(new Song(thisId, thisTitle, thisArtist, thisalbumId));
                // Add the info to our array.
               /* if (this.id == thisalbumId) {
                    songsInAlbum.add(new Song(thisId, thisTitle, thisArtist));
                }*/
            }
            while (musicCursor.moveToNext());
            //musicCursor.close();

            Collections.sort(songsInAlbum, new Comparator<Song>() {
                @Override
                public int compare(Song a, Song b) {
                    return a.getTitle().compareTo(b.getTitle());
                }
            });

            aslAdapter = new AlbumSongListAdapter(this,songsInAlbum);
            songListView.setAdapter(aslAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent musicPlayerIntent = new Intent(this,PlayerActivity.class);

            String songTitle = songsInAlbum.get(position).getTitle();
            musicPlayerIntent.putExtra("songTitle",songTitle);

            String tag = view.getTag().toString();
            musicPlayerIntent.putExtra("tag",tag);

            String songArtist = songsInAlbum.get(position).getArtist();
            musicPlayerIntent.putExtra("songArtist",songArtist);

            startActivity(musicPlayerIntent);
    }
}
