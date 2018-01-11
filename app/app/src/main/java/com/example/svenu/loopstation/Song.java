package com.example.svenu.loopstation;

/**
 * Created by svenu on 10-1-2018.
 */

public class Song {
    private String title;
    private String artist;
    private String url;

    public Song(String aTitle, String anArtist, String anUrl) {
        title = aTitle;
        artist = anArtist;
        url = anUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getUrl() {
        return url;
    }
}
