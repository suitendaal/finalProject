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
    }

    public void startRecording() {
        prepareMediaRecorder();
        recorder.start();
    }

    private void prepareMediaRecorder() {
        recorder = new MediaRecorder();

        String status = Environment.getExternalStorageState();
        if(status.equals("mounted")) {
            String path = pathName + samples.size();
            samples.add(path);

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(path);
            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Try","Exception");
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
