package com.example.administrator.musictest;

/**
 * Created by Gagan on 1/11/2017.
 */

public class Album {
    private String albumID;
    private String albumName;
    private String title;
    private String artistName;
    private String albumArtCover;

    public Album(String albumID, String albumName,  String title, String artistName, String albumArtCover) {
        this.albumID = albumID;
        this.albumName = albumName;
        this.artistName = artistName;
        this.title = title;
        this.albumArtCover = albumArtCover;
    }

    public Album(String albumID, String albumName) {
        this.albumID = albumID;
        this.albumName = albumName;
    }

    public String getAlbumArtCover() {
        return albumArtCover;
    }

    public void setAlbumArtCover(String albumArtCover) {
        this.albumArtCover = albumArtCover;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
