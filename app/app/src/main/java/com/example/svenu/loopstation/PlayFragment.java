package com.example.svenu.loopstation;


import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple subclass.
 */
public class PlayFragment extends DialogFragment {

    private View rootView;
    private Sample primarySample;
    private ArrayList<Sample> samples;
    private Button playPauseButton;
    private ToggleButton loopToggle;
    private TextView textView;
    private ActionChecker isPlaying;
    private boolean isLooping;
    private String songTitle;

    public PlayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_play, container, false);
        setVariables();
        setButtonHandlers();
        play();
        return rootView;
    }

    private class GoPlayPauseClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (isPlaying.getValue() == ActionChecker.notDoing) {
                play();
            }
            else {
                pause();
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        stop();
        super.onDismiss(dialog);
    }

    private void pause() {
        for (Sample sample: samples) {
            sample.pause();
        }
        isPlaying.setValue(ActionChecker.notDoing);
    }

    private void play() {
        for (Sample sample: samples) {
            sample.play();
        }
        primarySample.getMediaPlayer().setOnCompletionListener(new PlayCompletedListener());
    }

    private class PlayCompletedListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            stop();
            if (isLooping) {
                play();
            }
        }
    }

    private void setButtonHandlers() {
        playPauseButton.setOnClickListener(new GoPlayPauseClickListener());
        loopToggle.setOnCheckedChangeListener(new ToggleChangeListener());
    }

    private void setSamples(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (File file: files) {
            Sample sample = new Sample(file.getAbsolutePath());
            samples.add(sample);
        }
        primarySample = samples.get(0);
    }

    private void setVariables() {
        playPauseButton = rootView.findViewById(R.id.playPauseButton);
        loopToggle = rootView.findViewById(R.id.toggleButton);
        textView = rootView.findViewById(R.id.textViewTitle);
        samples = new ArrayList<>();
        Bundle args = getArguments();
        String path = args.getString("path");
        songTitle = args.getString("songTitle");
        textView.setText(songTitle);
        setSamples(path);
        isPlaying = new ActionChecker(ActionChecker.notDoing);
        isLooping = false;
    }

    private void stop() {
        for (Sample sample: samples) {
            sample.stop();
        }
        isPlaying.setValue(ActionChecker.notDoing);
    }

    private class ToggleChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            isLooping = b;
        }
    }
}
