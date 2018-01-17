package com.example.svenu.loopstation;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.LogRecord;

/**
 * Created by svenu on 16-1-2018.
 */

public class Sample {

    private String path;
    private int beginTime;
    private int duration;
    private MediaPlayer mediaPlayer;
    private boolean isMediaPlayerInitialized = false;

    public Sample(String aPath, int aBeginTime) {
        path = aPath;
        beginTime = aBeginTime;
        Log.d("beginTime", beginTime + "");
    }

    private void initializeMediaPlayer() {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDuration(int aDuration) {
        duration = aDuration;
    }

    public int getDuration() {
        return duration;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public String getPath() {
        return path;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void play() {
        // TODO: wait beginTime milliseconds

        if (!isMediaPlayerInitialized)
        {
            initializeMediaPlayer();
            isMediaPlayerInitialized = true;
        }
        mediaPlayer.start();
    }

    public void pause() {
        if (isMediaPlayerInitialized && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (isMediaPlayerInitialized) {
            mediaPlayer.release();
            isMediaPlayerInitialized = false;
        }
    }

    public File getSampleFile() {
        File file = new File(path);
        // TODO: add silence in length of beginTime
        return file;
    }
}
