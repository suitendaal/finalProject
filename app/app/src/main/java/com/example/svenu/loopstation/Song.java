package com.example.svenu.loopstation;

/**
 * Created by Sven Uitendaal.
 * Class which represents a song searched in searchactivity. It has an artist, a title and
 * a genius url.
 */

public class Song {
    private String artist;
    private String title;
    private String url;

    public Song(String aTitle, String anArtist, String anUrl) {
        title = aTitle;
        artist = anArtist;
        url = anUrl;
    }

    public String getArtist() {
        // Returns the artist.
        return artist;
    }

    public String getTitle() {
        // Returns the title.
        return title;
    }

    public String getUrl() {
        // Returns the url.
        return url;
    }
}
