package com.example.svenu.loopstation;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by svenu on 16-1-2018.
 */

public class Sample {

    private String path;
    private int duration;
    private MediaPlayer mediaPlayer;
    private boolean isMediaPlayerInitialized = false;
    private boolean isPlayable = false;

    public Sample(String aPath) {
        path = aPath;
        mediaPlayer = new MediaPlayer();
    }

    public int getDuration() {
        return duration;
    }

    public MediaPlayer getMediaPlayer() {
        if (!isMediaPlayerInitialized) {
            initializeMediaPlayer();
            isMediaPlayerInitialized = true;
        }
        return mediaPlayer;
    }

    public String getPath() {
        return path;
    }

    public File getSampleFile() {
        File file = new File(path);
        return file;
    }

    private void initializeMediaPlayer() {
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
        initializeMediaPlayer();
        duration = mediaPlayer.getDuration();
        isPlayable= true;
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void play() {
        if (!isMediaPlayerInitialized)
        {
            initializeMediaPlayer();
        }
        mediaPlayer.start();
    }

    public void stop() {
        if (isMediaPlayerInitialized) {
            mediaPlayer.stop();
            mediaPlayer.release();
            isMediaPlayerInitialized = false;
        }
    }
}
