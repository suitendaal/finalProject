package com.example.svenu.loopstation;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Sven Uitendaal.
 * Class to define a single recording sample. A sample has a mediaplayer which can play,
 * pause and stop the sample. It also has a function to delete itself.
 */

public class Sample {

    private int duration;
    private String path;
    private boolean isMediaPlayerInitialized = false;
    private boolean isPlayable = false;
    private MediaPlayer mediaPlayer;

    public Sample(String aPath) {
        path = aPath;
        mediaPlayer = new MediaPlayer();
    }

    public int getDuration() {
        // Returns the duration in milliseconds of a sample.
        return duration;
    }

    public MediaPlayer getMediaPlayer() {
        // Returns the mediaplayer of the sample.

        // If the mediaplayer is not initialized yet, initialize.
        if (!isMediaPlayerInitialized) {
            initializeMediaPlayer();
            isMediaPlayerInitialized = true;
        }
        return mediaPlayer;
    }

    public String getPath() {
        // Returns the absolute path.
        return path;
    }

    public File getSampleFile() {
        // Returns the file of the sample.
        return new File(path);
    }

    private void initializeMediaPlayer() {
        // Function to initialize the sample's mediaplayer.

        Log.d("initializeMediaPlayer", path);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isMediaPlayerInitialized = true;
    }

    public void initializeSample() {
        // Function to initialize the sample.

        initializeMediaPlayer();
        duration = mediaPlayer.getDuration();
        isPlayable= true;
    }

    public boolean isPlayable() {
        // Returns if the sample is initialized.
        return isPlayable;
    }

    public void pause() {
        // Pauses the sample.
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void play() {
        // Play the sample.

        // First ensure the mediaplayer is initialized.
        if (!isMediaPlayerInitialized)
        {
            initializeMediaPlayer();
        }
        mediaPlayer.start();
    }

    public void stop() {
        // Stop playing the sample.

        // Release the mediaplayer.
        if (isMediaPlayerInitialized) {
            mediaPlayer.stop();
            mediaPlayer.release();
            isMediaPlayerInitialized = false;
        }
    }

    public void delete() {
        // Function to delete the sample.

        File file = new File(path);
        if (file.delete()) {
            Log.d("deleted", path);
        }
    }
}
