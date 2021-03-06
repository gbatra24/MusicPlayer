package com.example.administrator.musictest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Gagan on 11/17/2016.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPosn, length;
    private final IBinder musicBind = new MusicBinder();
    private Random random;

    @Override
    public void onCreate() {
        super.onCreate();
        songPosn = 0;
        player = new MediaPlayer();
        random = new Random();
        initMusicPlayer();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
    }

    public class MusicBinder extends Binder {

        MusicService getService() {
            return MusicService.this;
        }
    }

    public Song getCurrentPlayingSong() {
        return songs.get(songPosn);
    }

    public Song getSong(int position) {
        return songs.get(position);
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    public void pausePlayer() {
        player.pause();
        length = player.getCurrentPosition();
    }

    public void playShuffled() throws IOException {
        songPosn = random.nextInt(songs.size());
        setSong(songPosn);
        playSong();
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public int getCurrentSongDuration() {
        return player.getDuration();
    }

    public int getPosn() {
        return player.getCurrentPosition();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void resumePlayer() {
        player.start();
    }

    public void playPrev() throws IOException {
        decrementSong();
        playSong();
    }

    public String updateTitle() {
        return songs.get(songPosn).getTitle();
    }

    public String updateArtist() {
        return songs.get(songPosn).getArtist();
    }

    public void incrementSongCount() {
        songPosn++;
        if (songPosn >= songs.size()) songPosn = 0;
    }

    public void decrementSong() {
        songPosn--;
        if(songPosn < 0) {
            songPosn = songs.size()-1;
        }
    }

    public void playNext() throws IOException {
        incrementSongCount();
        playSong();
    }

    public void playSong() throws IOException {

        player.reset();

        Song playSong = songs.get(songPosn);

        long currSong = playSong.getId();

        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepare();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            mp.reset();

            playNext();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setTicker(updateTitle())
                .setOngoing(true)
                .setContentTitle(updateTitle())
                .setContentText(updateArtist());

        Notification not = builder.build();
        startForeground(1,not);
    }

}
