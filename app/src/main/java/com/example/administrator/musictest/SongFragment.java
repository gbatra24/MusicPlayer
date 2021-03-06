package com.example.administrator.musictest;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Gagan on 11/21/2016.
 */
public class SongFragment extends Fragment {
    private static final int MY_READ_EXTERNAL_PERMISSION_CONSTANT = 1;
    private ArrayList<Song> songList;
    private FastScrollRecyclerView songView;
    private MusicService musicSrv;
    private boolean musicBound = false;
    private Intent playIntent;
    Song mSong;
    private String coverPath;
    private EditText searchBox;
    private SongAdapter songAdt;
    private LinearLayoutManager mLayoutManager;
    private int lastPage = Integer.MAX_VALUE;
    private int currentPage = 0;
    private boolean isLoading = false;
    private final int PAGE_SIZE = 50;

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && currentPage < lastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    loadMoreItems();
                }
            }
        }
    };

    private void loadMoreItems() {
        isLoading = true;

        currentPage += 1;

        fillSongAdapter(currentPage, PAGE_SIZE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songs_fragment, container, false);
        songView = (FastScrollRecyclerView) view.findViewById(R.id.songs_list);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        songView.setLayoutManager(mLayoutManager);
        // Pagination
        songView.addOnScrollListener(recyclerViewOnScrollListener);

        songList = new ArrayList<Song>();
        //searchBox = (EditText) view.findViewById(R.id.search_box);

        songAdt = new SongAdapter(songList);
        songView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        songView.setAdapter(songAdt);

        getSongList();
        //fetchAlbumArt();

        // songView.setOnItemClickListener(this);
        //searchBox.addTextChangedListener(this);
        return view;
    }

    public int getLastPage() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor countCursor = musicResolver.query(musicUri, new String[] {"count(*) AS count"}, null, null, null);
        countCursor.moveToFirst();
        return (int) Math.ceil(countCursor.getInt(0) / PAGE_SIZE);
    }

    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(getActivity(), MusicService.class);
            this.getActivity().bindService(playIntent, musicConnection, BIND_AUTO_CREATE);
            this.getActivity().startService(playIntent);
        }
    }

    @Override
    public void onDestroy() {
        musicSrv.stopService(playIntent);
        this.getActivity().unbindService(musicConnection);
        musicSrv = null;
        super.onDestroy();
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;

            musicSrv = binder.getService();
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public ArrayList<Song> fetchList() {
        return songList;
    }

    public void getSongList() {

        int permissionCheckRead = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_PERMISSION_CONSTANT);
        } else {
            fillSongAdapter(currentPage, PAGE_SIZE);
            lastPage = getLastPage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_READ_EXTERNAL_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fillSongAdapter(currentPage, PAGE_SIZE);
                lastPage = getLastPage();
            }
        } else {
            System.out.println("Permission is denied............................");
        }

    }

    public void fillSongAdapter(int pageNumber, int pageSize) {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        int offset = pageNumber * pageSize;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null,
                MediaStore.Audio.Media.TITLE + " LIMIT " + offset + ", " + pageSize);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumArtColumn = musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID);

            do {
                long thisID = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbumId = musicCursor.getString(albumArtColumn);

                Cursor albumCursor = musicResolver.query(albumUri,
                        new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID + "=?",
                        new String[]{String.valueOf(thisAlbumId)},
                        null);
                if (albumCursor != null && albumCursor.moveToFirst()) {
                    thisAlbumId = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }

                songList.add(new Song(thisID, thisTitle, thisArtist, thisAlbumId));
            } while (musicCursor.moveToNext());

//            Collections.sort(songList, new Comparator<Song>() {
//                @Override
//                public int compare(Song a, Song b) {
//                    return a.getTitle().compareTo(b.getTitle());
//                }
//            });

            //songAdt = new SongAdapter(songList);
            songView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), songView, new ClickListener() {
                @Override
                public void onClick(View childView, int Position) {
                    //Toast.makeText(getActivity(),"onClick "+Position,Toast.LENGTH_SHORT).show();
                    if (musicSrv.isPlaying()) {
                        musicSrv.pausePlayer();
                    } else {

                        Intent musicPlayerIntent = new Intent(getActivity(), PlayerActivity.class);

                        String songTitle = songList.get(Position).getTitle();
                        musicPlayerIntent.putExtra("songTitle", songTitle);

                        String mPosition = String.valueOf(Position);
                        musicPlayerIntent.putExtra("mPosition", mPosition);

                  /*  String tag = childView.getTag().toString();
                    musicPlayerIntent.putExtra("tag",tag);*/

                        String songArtist = songList.get(Position).getArtist();
                        musicPlayerIntent.putExtra("songArtist", songArtist);

                        startActivity(musicPlayerIntent);


                    }
                }
            }));



            songAdt.notifyDataSetChanged();
            isLoading = false;

        }
    }


    /* @Override
     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         if(musicSrv.isPlaying()){
             musicSrv.pausePlayer();
         }
         else {

             Intent musicPlayerIntent = new Intent(this.getActivity(),PlayerActivity.class);

             String songTitle = songList.get(position).getTitle();
             musicPlayerIntent.putExtra("songTitle",songTitle);

             String tag = view.getTag().toString();
             musicPlayerIntent.putExtra("tag",tag);

             String songArtist = songList.get(position).getArtist();
             musicPlayerIntent.putExtra("songArtist",songArtist);

             startActivity(musicPlayerIntent);


         }

     }
 */
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, RecyclerView recyclerView, ClickListener clickListener) {
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

    public void filterResults(String newText) {
        if (TextUtils.isEmpty(newText)) {
            songAdt.getFilter().filter(newText);
        } else {
            songAdt.getFilter().filter(newText.toString());
        }
        //return true;
    }

}
