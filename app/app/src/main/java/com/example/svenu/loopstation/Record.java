package com.example.svenu.loopstation;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by svenu on 18-1-2018.
 */

public class Record {

    private String pathName;
    private String fileFormat;
    private ToggleButton toggleButton;
    private MediaRecorder recorder;
    private ArrayList<Sample> samples;
    private boolean firstRecord;
    private ActionChecker isPlaying;
    private ActionChecker isRecording;

    private final int bitRate = 16;
    private final int sampleRate = 44100;

    public Record(String aPathName, String aFileFormat, ToggleButton aToggleButton) {
        pathName = aPathName;
        fileFormat = aFileFormat;
        toggleButton = aToggleButton;
        recorder = new MediaRecorder();
        samples = new ArrayList<>();
        firstRecord = true;
        isPlaying = new ActionChecker(ActionChecker.notDoing);
        isRecording = new ActionChecker(ActionChecker.notDoing);
    }

    public void buttonClicked() {
        // if recording, stop recording
        if (isRecording.getValue() == ActionChecker.doing) {
            stopRecording();
        }
        else if (isRecording.getValue() == ActionChecker.notDoing) {
            // if not recording and not playing (stop or pause), start recording
            if (isPlaying.getValue() != ActionChecker.doing) {
                startRecording();
            }
            // if not recording and playing, wait for loop to end, then record
            else {
                isRecording.setValue(ActionChecker.goingToDo);
                toggleButton.setChecked(false);
                isPlaying.setVariableChangeListener(new IsPlayingChangeListener());
            }
        }
        // if already going to record, do nothing
        else {
            toggleButton.setChecked(false);
        }
    }

    public void close() {
        stop();
        if (isRecording.getValue() != ActionChecker.notDoing) {
            stopRecording();
        }
        isRecording.setValue(ActionChecker.notDoing);
    }

    private class GoMediaPlayerCompleted implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            isPlaying.setValue(ActionChecker.notDoing);
            play();
        }
    }

    private void initializeRecorder(String path) {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncodingBitRate(bitRate);
        recorder.setAudioSamplingRate(sampleRate);
        recorder.setOutputFile(path);
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class IsPlayingChangeListener implements ActionChecker.VariableChangeListener {
        @Override
        public void onVariableChanged(ActionChecker anIsPlaying) {
            if (isPlaying.getValue() == ActionChecker.notDoing && isRecording.getValue() == ActionChecker.goingToDo) {
                startRecording();
            }
        }
    }

    public void pause() {
        if (isPlaying.getValue() == ActionChecker.doing) {
            for (Sample sample: samples) {
                sample.pause();
            }
            isPlaying.setValue(ActionChecker.goingToDo);
            if (isRecording.getValue() == ActionChecker.doing) {
                stopRecording();
            }
        }
        else if (isPlaying.getValue() == ActionChecker.goingToDo) {
            for (Sample sample: samples) {
                sample.play();
            }
            isPlaying.setValue(ActionChecker.doing);
        }
    }

    public void play() {
        if (samples.size() > 0 && isPlaying.getValue() != ActionChecker.doing) {
            for (Sample sample : samples) {
                if (sample.isPlayable()) {
                    Log.d("playing ", sample.getPath());
                    sample.play();
                }
            }
            Sample primarySample = samples.get(0);
            if (primarySample.isPlayable()) {
                isPlaying.setValue(ActionChecker.doing);
                primarySample.getMediaPlayer().setOnCompletionListener(new GoMediaPlayerCompleted());
            }
        }
    }

    private class RecorderStopListener implements MediaRecorder.OnInfoListener {
        @Override
        public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                Log.d("max duration", "reached");
                stopRecording();
            }
        }
    }

    public void saveRecord(String fileName) {
        close();
        ArrayList<File> files = new ArrayList<>();
        for (Sample sample: samples) {
            File file = sample.getSampleFile();
            files.add(file);
        }
        SongCreator songCreator = new SongCreator();
        songCreator.createSong(fileName, files);
    }

    private void startRecording() {
        String status = Environment.getExternalStorageState();
        if(status.equals("mounted")) {
            String path = pathName + "/" + samples.size() + "." + fileFormat;
            if (!firstRecord) {
                if (isPlaying.getValue() == ActionChecker.goingToDo) {
                    stop();
                }
                play();
            }

            initializeRecorder(path);
            samples.add(new Sample(path));
            recorder.start();
            isRecording.setValue(ActionChecker.doing);
            recorder.setOnInfoListener(new RecorderStopListener());
            toggleButton.setChecked(true);
        }
    }

    public void stop() {
        if (isPlaying.getValue() != ActionChecker.notDoing) {
            for (Sample sample: samples) {
                sample.stop();
            }
        }
        isPlaying.setValue(ActionChecker.notDoing);
        if (isRecording.getValue() == ActionChecker.doing) {
            stopRecording();
        }
    }

    private void stopRecording() {
        isRecording.setValue(ActionChecker.notDoing);
        toggleButton.setChecked(false);
        recorder.stop();
        Sample sample = samples.get(samples.size() - 1);
        sample.initializeSample();
        if (firstRecord) {
            firstRecord = false;
            recorder.setMaxDuration(sample.getDuration());
        }
        toggleButton.setChecked(false);
    }
}
