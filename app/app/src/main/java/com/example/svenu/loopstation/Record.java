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

    private String fileFormat;
    private boolean firstRecord;
    private ActionChecker isPlaying;
    private ActionChecker isRecording;
    private String pathName;
    private ToggleButton playPauseButton;
    private ArrayList<Sample> samples;
    private ToggleButton recordButton;
    private MediaRecorder recorder;

    // Bit and samplerate for recording.
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
            try {
                stopRecording();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
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
        // Stop playing and stop recording.
        stop();
        if (isRecording.getValue() != ActionChecker.notDoing) {
            try {
                stopRecording();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public void buttonPlayPauseClicked() {
        // Tell the record to play or pause the samples.
        if (isPlaying.getValue() == ActionChecker.doing) {
            pause();
        }
        else {
            play();
        }
    }

    public void delete() {
        // Function to reset the record.
        for (Sample sample: samples) {
            if (sample.isPlayable()) {
                sample.stop();
            }
            sample.delete();
        }
        setVariables();
    }

    public int getSampleSize() {
        // Get number of samples.
        return samples.size();
    }

    private class GoMediaPlayerCompleted implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // When the first sample is completed, stop all of the samples and play them again.
            if (isPlaying.getValue() == ActionChecker.doing) {
                for (Sample sample : samples) {
                    if (sample.isPlayable()) {
                        sample.stop();
                    }
                }
                isPlaying.setValue(ActionChecker.notDoing);
                play();
            }
        }
    }

    private void initializeRecorder(String path) {
        // Function to initialize the mediarecorder.
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
            // If recording is available, start recording.
            if (isPlaying.getValue() == ActionChecker.notDoing && isRecording.getValue() == ActionChecker.goingToDo) {
                startRecording();
            }
        }
    }

    public void pause() {
        // Function to pause the samples.

        if (isPlaying.getValue() == ActionChecker.doing) {

            // Pause the samples
            for (Sample sample: samples) {
                sample.pause();
            }
            isPlaying.setValue(ActionChecker.goingToDo);
            playPauseButton.setChecked(false);

            // Stop recording
            if (isRecording.getValue() == ActionChecker.doing) {
                try {
                    stopRecording();
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
            recordButton.setChecked(false);
        }
    }

    private void play() {
        // Function to play the samples.
        if (isPlaying.getValue() != ActionChecker.doing) {
            if (samples.size() > 0) {

                // Play all the samples.
                for (Sample sample : samples) {
                    if (sample.isPlayable()) {
                        Log.d("playing ", sample.getPath());
                        sample.play();
                    }
                }

                // Wait for when the first sample is finished.
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
                // When max recording duration is reached, ensure the recorder stops recording.
                Log.d("max duration", "reached");
                try {
                    stopRecording();
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveRecord(String fileName) {
        // Function to save the record.
        close();

        // Get all the sample files.
        ArrayList<File> files = new ArrayList<>();
        for (Sample sample: samples) {
            File file = sample.getSampleFile();
            files.add(file);
        }

        // Create a song in the given path.
        SongCreator songCreator = new SongCreator();
        try {
            songCreator.createSong(fileName, files);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setVariables() {
        // Function to set global variables.
        recorder = new MediaRecorder();
        samples = new ArrayList<>();
        firstRecord = true;
        isPlaying = new ActionChecker(ActionChecker.notDoing);
        isRecording = new ActionChecker(ActionChecker.notDoing);

        // Set buttons unchecked.
        recordButton.setChecked(false);
        playPauseButton.setChecked(false);
    }

    private void startRecording() throws IllegalStateException {
        // Function to start recording.
        String status = Environment.getExternalStorageState();
        if(status.equals("mounted")) {
            String path = pathName + "/" + samples.size() + "." + fileFormat;

            // Check if there exist samples already.
            if (!firstRecord) {
                // If samples are pausing, stop the samples so they start playing at their begin.
                if (isPlaying.getValue() == ActionChecker.goingToDo) {
                    stop();
                }

                // Play the samples.
                play();
            }

            // Initialize the recorder.
            initializeRecorder(path);

            // Create a new sample.
            samples.add(new Sample(path));

            // Start recording.
            recorder.start();
            isRecording.setValue(ActionChecker.doing);
            recorder.setOnInfoListener(new RecorderStopListener());
            recordButton.setChecked(true);
        }
    }

    public void stop() {
        // Function to stop playing, recording.

        // Stop the samples from playing.
        if (isPlaying.getValue() != ActionChecker.notDoing) {
            for (Sample sample: samples) {
                if (sample.isPlayable()) {
                    sample.stop();
                }
            }
        }
        isPlaying.setValue(ActionChecker.notDoing);
        playPauseButton.setChecked(false);

        // Stop the samples from recording.
        if (isRecording.getValue() == ActionChecker.doing) {
            try {
                stopRecording();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        recordButton.setChecked(false);
    }

    private void stopRecording() throws RuntimeException {
        // Function to stop recording.

        // Stop recording.
        if (isRecording.getValue() == ActionChecker.doing) {
                recorder.stop();
        }
        isRecording.setValue(ActionChecker.notDoing);
        recordButton.setChecked(false);

        // Initialize the new sample.
        Sample sample = samples.get(samples.size() - 1);
        sample.initializeSample();

        // If it is the first sample, set max duration time.
        if (firstRecord) {
            firstRecord = false;
            recorder.setMaxDuration(sample.getDuration());
            play();
        }
    }
}
