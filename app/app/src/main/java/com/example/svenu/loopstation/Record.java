package com.example.svenu.loopstation;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by svenu on 15-1-2018.
 */

public class Record {
    private Context context;
    private ArrayList<Sample> samples;
    private MediaRecorder recorder;
    private String pathName;
    private boolean firstRecord = true;
    private boolean isPlaying = false;
    private long time;
    private int numberOfCompletedSamples = 0;
    private int maxDuration;

    public Record(Context aContext, String aPathName) {
        context = aContext;
        samples = new ArrayList<>();
        pathName = aPathName;
        recorder = new MediaRecorder();
    }

    public void startRecording() {
        if (!isPlaying && !firstRecord) {
            play();
        }
        prepareMediaRecorder();
        recorder.start();
    }

    private void prepareMediaRecorder() {
        String status = Environment.getExternalStorageState();
        if(status.equals("mounted")) {
            String path = pathName + "/" + samples.size() + ".mpeg4";
            int beginTime = 0;
            if (!firstRecord) {
                beginTime = (int) (System.currentTimeMillis() - time);
                recorder.setMaxDuration(maxDuration - beginTime);
            }

            samples.add(new Sample(path, beginTime));

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            recorder.setOutputFile(path);
            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecording() {
        recorder.stop();
        setDurationTime();
        if (firstRecord) {
            firstRecord = false;
        }
    }

    private void setDurationTime() {
        Sample sample = samples.get(samples.size() - 1);

        // https://stackoverflow.com/questions/15394640/get-duration-of-audio-file
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(sample.getPath()));
        int duration = mediaPlayer.getDuration();
        sample.setDuration(duration);
        if (firstRecord) {
            maxDuration = duration;
        }
    }

    public void saveRecord(String fileName) {
        stopRecording();
        recorder.release();
        // TODO: get all samples and save them to one audio file
    }

    public void play() {
        numberOfCompletedSamples = 0;
        if (!isPlaying) {
            for (Sample sample : samples) {
                Log.d("playing", sample.getPath());
                sample.play();
                sample.getMediaPlayer().setOnCompletionListener(new mediaPlayedListener());
            }
            isPlaying = true;
            time = System.currentTimeMillis();
        }
    }

    public void pause() {
        for (Sample sample: samples) {
            sample.pause();
        }
        isPlaying = false;
    }

    public void stopPlaying() {
        for (Sample sample: samples) {
            sample.stop();
        }
        isPlaying = false;
    }

    private class mediaPlayedListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            numberOfCompletedSamples += 1;
            if (numberOfCompletedSamples == samples.size()) {
                isPlaying = false;
                play();
            }
        }
    }

    // TODO: plan om files te mergen: als er niets wordt afgespeeld en je drukt op record neemt hij op.
    // Als je daarna niet meer aan het recorden bent blijft hij doorspelen (play stand)
    // De play stand is als hij bezig is met afspelen en de stop stand is als hij niets doet.
    // Als je in de stop stand op record drukt speelt hij af en neemt hij gelijk op.
    // Als je in de play stand op record drukt wacht hij eerst tot de ronde af is voordat hij opneemt,
    // de toggle gaat dan ook niet aan, pas als hij begint met opnemen.
}
