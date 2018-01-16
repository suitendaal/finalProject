package com.example.svenu.loopstation;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by svenu on 15-1-2018.
 */

public class Record {
    private ArrayList<String> samples;
    private MediaRecorder recorder;
    private String pathName;

    public Record(String aPathName) {
        samples = new ArrayList<>();
        pathName = aPathName;
        recorder = new MediaRecorder();
    }

    public void startRecording() {
        prepareMediaRecorder();
        recorder.start();
    }

    private void prepareMediaRecorder() {
        String status = Environment.getExternalStorageState();
        if(status.equals("mounted")) {
            String path = pathName + samples.size() + ".mpeg4";
            samples.add(path);

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
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
    }

    public void saveRecord() {
        recorder.release();
        //TODO: take all samples and convert them to audiofile
    }
}
