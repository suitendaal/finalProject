package com.example.svenu.loopstation;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class which has a mediarecorder to record samples. When a sample is recorder it is added to
 * the record's collection of samples. It can record and stop recording a sample.
 * The maximum duration time is the time of the first recorded sample.
 * It can play, pause and stop the samples. When a user saves his record,
 * all of the samples are moved to another directory, chosen by the user.
 */

public class Record {

    private String pathName;
    private String fileFormat;
    private ToggleButton recordButton;
    private ToggleButton playPauseButton;
    private MediaRecorder recorder;
    private ArrayList<Sample> samples;
    private boolean firstRecord;
    private ActionChecker isPlaying;
    private ActionChecker isRecording;

    private final int bitRate = 16;
    private final int sampleRate = 44100;

    public Record(String aPathName, String aFileFormat, ToggleButton aRecordButton, ToggleButton aPlayPauseButton) {
        pathName = aPathName;
        fileFormat = aFileFormat;
        recordButton = aRecordButton;
        playPauseButton = aPlayPauseButton;
        setVariables();
    }

    public void buttonRecordClicked() {
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
                recordButton.setChecked(false);
                isPlaying.setVariableChangeListener(new IsPlayingChangeListener());
            }
        }
        // if already going to record, do nothing
        else {
            recordButton.setChecked(false);
        }
    }

    public void close() {
        stop();
        if (isRecording.getValue() != ActionChecker.notDoing) {
            stopRecording();
        }
        isRecording.setValue(ActionChecker.notDoing);
    }

    public void buttonPlayPauseClicked() {
        if (isPlaying.getValue() == ActionChecker.doing) {
            pause();
        }
        else {
            play();
        }
    }

    public void delete() {
        for (Sample sample: samples) {
            if (sample.isPlayable()) {
                sample.stop();
            }
            sample.delete();
        }
        setVariables();
    }

    private class GoMediaPlayerCompleted implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            for (Sample sample: samples) {
                if (sample.isPlayable()) {
                    sample.stop();
                }
            }
            isPlaying.setValue(ActionChecker.notDoing);
            play();
        }
    }

    private void initializeRecorder(String path) {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setAudioEncodingBitRate(bitRate);
        recorder.setAudioSamplingRate(sampleRate);
        recorder.setOutputFile(path);
        try {
            recorder.prepare();
        } catch (IllegalStateException | IOException e) {
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
            playPauseButton.setChecked(false);
        }
    }

    private void play() {
        if (isPlaying.getValue() != ActionChecker.doing) {
            if (samples.size() > 0) {
                for (Sample sample : samples) {
                    if (sample.isPlayable()) {
                        Log.d("playing ", sample.getPath());
                        sample.play();
                        Log.d("duration", sample.getDuration() + "");
                    }
                }
                Sample primarySample = samples.get(0);
                if (primarySample.isPlayable()) {
                    isPlaying.setValue(ActionChecker.doing);
                    primarySample.getMediaPlayer().setOnCompletionListener(new GoMediaPlayerCompleted());
                }
                playPauseButton.setChecked(true);
            }
            else {
                playPauseButton.setChecked(false);
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
        try {
            songCreator.createSong(fileName, files);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setVariables() {
        recorder = new MediaRecorder();
        samples = new ArrayList<>();
        firstRecord = true;
        isPlaying = new ActionChecker(ActionChecker.notDoing);
        isRecording = new ActionChecker(ActionChecker.notDoing);

        recordButton.setChecked(false);
        playPauseButton.setChecked(false);
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
            recordButton.setChecked(true);
        }
    }

    public void stop() {
        if (isPlaying.getValue() != ActionChecker.notDoing) {
            for (Sample sample: samples) {
                if (sample.isPlayable()) {
                    sample.stop();
                }
            }
        }
        isPlaying.setValue(ActionChecker.notDoing);
        if (isRecording.getValue() == ActionChecker.doing) {
            stopRecording();
        }
        playPauseButton.setChecked(false);
    }

    private void stopRecording() {
        isRecording.setValue(ActionChecker.notDoing);
        recordButton.setChecked(false);
        recorder.stop();
        Sample sample = samples.get(samples.size() - 1);
        sample.initializeSample();
        if (firstRecord) {
            firstRecord = false;
            recorder.setMaxDuration(sample.getDuration());
            play();
        }
    }
}
