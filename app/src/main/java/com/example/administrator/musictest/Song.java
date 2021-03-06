package com.example.administrator.musictest;

/**
 * Created by Gagan on 11/16/2016.
 */

public class Song {

    private long id;
    private String title;
    private String artist;
    private String album;
    private String albumID;

    public Song(long songID , String songTitle, String artistName, String albumId) {
        id = songID;
        title = songTitle;
        artist = artistName;
        albumID = albumId;
    }

    public Song(long artistID , String artistName) {
        id = artistID;
        artist = artistName;
    }

    public Song (String songTitle, String albumName) {
        title = songTitle;
        album = albumName;
    }

    public Song(long thisId, String albumName, String albumId) {
        id = thisId;
        album = albumName;
        albumID = albumId;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        return albumID.equals(song.albumID);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + (albumID != null ? albumID.hashCode() : 0);
        return result;
    }*/
    /*

    public Song(long albumID, String albumName) {
        id = albumID;
        album = albumName;
    }*/

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return  artist;
    }

    public String getAlbumId() { return albumID; }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbumId(String albumId) {
        albumID = albumId;
    }

}
