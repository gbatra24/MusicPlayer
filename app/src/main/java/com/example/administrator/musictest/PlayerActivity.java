package com.example.administrator.musictest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Toolbar myToolbar;
    private MediaPlayer mediaPlayer;
    private ImageButton playpause, next, previous,shuffle;
    private ImageView albumCover;
    private SeekBar progressSeekbar;
    private AudioManager audioManager;
    private String songDurationString;
    private int songPosn;
    private boolean musicBound = false, shuffledPlayFlag = false;

    private Intent playIntent;

    private MusicService musicService;
    private ArrayList<Song> songs;
    private TextView textViewTitle, textViewArtist,textViewSongDuration;
    private Handler mHandler;
    int mPosition;
    //Bitmap bitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewTitle = (TextView) findViewById(R.id.tv_song_title);
        textViewArtist = (TextView) findViewById(R.id.tv_song_artist);
        playpause = (ImageButton) findViewById(R.id.playButton);
        previous = (ImageButton) findViewById(R.id.previousButton);
        next = (ImageButton) findViewById(R.id.nextButton);
        shuffle = (ImageButton) findViewById(R.id.shuffle_button);
        albumCover = (ImageView) findViewById(R.id.album_cover);
        progressSeekbar = (SeekBar) findViewById(R.id.progressSeekbar);
        //textViewSongCount = (TextView) findViewById(R.id.tv_song_count);
        textViewSongDuration = (TextView) findViewById(R.id.song_duration);

        mHandler = new Handler();
        playpause.setBackgroundResource(R.drawable.circledpause);
        playpause.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);

        String receivedSongTitle = getIntent().getStringExtra("songTitle");
        textViewTitle.setText(receivedSongTitle);

        String receivedSongArtist = getIntent().getStringExtra("songArtist");
        textViewArtist.setText(receivedSongArtist);

        mPosition = Integer.parseInt(getIntent().getStringExtra("mPosition"));

       /* String receivedTag = getIntent().getStringExtra("tag");
        songPosn = Integer.parseInt(receivedTag);*/

        //String coverPath = getIntent().getStringExtra("coverPath");
        progressSeekbar.setOnSeekBarChangeListener(this);
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shuffledPlayFlag){
                    shuffledPlayFlag = true;
                    shuffle.setImageResource(android.R.drawable.btn_minus);
                }
                else {
                    shuffledPlayFlag = false;
                    shuffle.setImageResource(android.R.drawable.btn_plus);
                }
            }
        });

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
            mHandler.removeCallbacks(mUpdateTimeTask);
        }
        return super.onOptionsItemSelected(item);
    }

    public int getDurations() {
        if(musicService!=null && musicBound) {
            return musicService.getCurrentSongDuration();
        }
        else return 0;
    }

    public void shuffledPlay() throws IOException {
        musicService.playShuffled();
    }

    public long getCurrentSongPosition() {
        return musicService.getPosn();
    }

    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";
        String minutesString = "";

        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);

        if(hours > 0){
            finalTimerString = hours + ":";
        }

        if(seconds < 10){
            secondsString = "0" + seconds;
        }
        else{
            secondsString = "" + seconds;
        }

        if(minutes < 10){
            minutesString = "0" + minutes;
        }
        else{
            minutesString = "" + minutes;
        }

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

        return finalTimerString;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            this.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            this.startService(playIntent);
        }
        musicBound = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        musicService.stopService(playIntent);
        this.unbindService(musicConnection);
        musicService=null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            songPosn = mPosition;

            try {
                musicService.setSong(songPosn);
                playSong();
            } catch (IOException e) {
                e.printStackTrace();
            }

            songDurationString = milliSecondsToTimer(getDurations());
            String currentPosition = String.valueOf(musicService.getPosn())+"/"+songDurationString;
            textViewSongDuration.setText(currentPosition);

//            Song currentPlayingSong = musicService.getSong(songPosn);
//            Drawable drawable = Drawable.createFromPath(currentPlayingSong.getAlbumId());
//            albumCover.setImageDrawable(drawable);
//            updateProgressBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (!musicService.isPlaying()) {
                return;
            }
            long totalDuration = getDurations();
            long currentDuration = getCurrentSongPosition();

            textViewSongDuration.setText(milliSecondsToTimer(currentDuration)+"/"+milliSecondsToTimer(totalDuration));
            int progress = (int)(getProgressPercentage(currentDuration, totalDuration));
            progressSeekbar.setProgress(progress);

            String updateTitle = musicService.updateTitle();
            textViewTitle.setText(updateTitle);

            String updateArtist = musicService.updateArtist();
            textViewArtist.setText(updateArtist);

            Song currentPlayingSong = musicService.getCurrentPlayingSong();
            /*Drawable drawable = Drawable.createFromPath(currentPlayingSong.getAlbumId());
            albumCover.setImageDrawable(drawable);*/
            Bitmap bit = BitmapFactory.decodeFile(currentPlayingSong.getAlbumId());
            if(bit != null ) {
                albumCover.setImageBitmap(bit);
            }
            else {
                albumCover.setImageResource(R.drawable.default_cover);
            }

            mHandler.postDelayed(this, 1000);
        }
    };

    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;
        percentage =(((double)currentDuration)/totalDuration)*100;
        return percentage.intValue();
    }

    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        return currentDuration * 1000;
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }

    public void playSong() throws IOException {
        musicService.playSong();
        progressSeekbar.setProgress(0);
        progressSeekbar.setMax(100);

        updateProgressBar();

        Song currentPlayingSong = musicService.getCurrentPlayingSong();
        /*Drawable drawable = Drawable.createFromPath(currentPlayingSong.getAlbumId());
        albumCover.setImageDrawable(drawable);*/
        Bitmap bit = BitmapFactory.decodeFile(currentPlayingSong.getAlbumId());
        if(bit != null ) {
            albumCover.setImageBitmap(bit);
        }
        else {
            albumCover.setImageResource(R.drawable.default_cover);
        }
    }

    public void playNextSong() throws IOException {
        if(shuffledPlayFlag) {
            shuffledPlay();
        }
        else {
            musicService.incrementSongCount();
            this.playSong();
        }
        updateProgressBar();
    }

    public void playPreviousSong() throws IOException {
        if(shuffledPlayFlag) {
            shuffledPlay();
        }
        else {
            musicService.decrementSong();
            this.playSong();
        }
        updateProgressBar();
    }

    @Override
    public void onClick(View v) {
       switch(v.getId()) {

            case R.id.playButton:
                if (musicService.isPlaying()) {
                    musicService.pausePlayer();
                    playpause.setBackgroundResource(R.drawable.circledplaybig);
                    updateProgressBar();
                } else {
                    musicService.resumePlayer();
                    playpause.setBackgroundResource(R.drawable.circledpause);
                    updateProgressBar();
                }

                break;

            case R.id.nextButton:
                try {
                    playNextSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

           case R.id.previousButton: // not working
               try {
                   playPreviousSong();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               break;
           default:
        }
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }



    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = getDurations();
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

        musicService.seek(currentPosition);
        updateProgressBar();
    }
}
