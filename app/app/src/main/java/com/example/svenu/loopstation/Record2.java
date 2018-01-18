package com.example.svenu.loopstation;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by svenu on 18-1-2018.
 */

public class Record2 {

    private ArrayList<Sample> samples;
    private Context context;
    private String pathName;
    private MediaRecorder recorder;
    private int maxDurationTime;
    private boolean firstRecord = true;
    private IsPlaying isPlaying = new IsPlaying(false);
    private IsPlaying isRecording = new IsPlaying(false);

    public Record2(Context aContext, String aPathName) {
        context = aContext;
        pathName = aPathName;
        samples = new ArrayList<>();
        recorder = new MediaRecorder();

        //isPlaying.setVariableChangeListener(new BooleanChangeListener(), toggleButton);
    }

    public void play() {
        if (samples.size() > 0 && !isPlaying.getValue()) {
            for (Sample sample : samples) {
                if (sample.isPlayable()) {
                    sample.play();
                }
            }
            Sample primarySample = samples.get(0);
            if (primarySample.isPlayable()) {
                isPlaying.setValue(true);
                primarySample.getMediaPlayer().setOnCompletionListener(new GoMediaPlayerCompleted());
            }
        }
    }

    private class GoMediaPlayerCompleted implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            isPlaying.setValue(false);
            play();
        }
    }

    public void pause() {
        if (isPlaying.getValue()) {
            for (Sample sample: samples) {
                sample.pause();
            }
        }
    }

    public void stop() {
        if (isPlaying.getValue()) {
            for (Sample sample: samples) {
                sample.stop();
            }
        }
    }

    public void startRecording(ToggleButton toggleButton) {
        if (!isPlaying.getValue()) {

        }
        else {

        }
    }

    public void record(ToggleButton toggleButton) {

    }

    private class BooleanChangeListener implements IsPlaying.VariableChangeListener {
        @Override
        public void onVariableChanged(IsPlaying aVariable) {

        }
    }

    public void stopRecording(ToggleButton toggleButton) {
        toggleButton.setChecked(false);
        recorder.stop();
        Sample sample = samples.get(samples.size() - 1);
        sample.initializeSample();
        if (firstRecord) {
            maxDurationTime = sample.getDuration();
            firstRecord = false;
        }
    }

    public void saveRecord(String fileName) {

    }
}
