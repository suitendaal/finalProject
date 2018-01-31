package com.example.svenu.loopstation;


import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sven Uitendaal.
 * Fragment to play and loop a saved recording.
 */
public class PlayFragment extends DialogFragment {

    private boolean isPlaying;
    private boolean isLooping;
    private ToggleButton loopToggle;
    private ToggleButton playPauseButton;
    private Sample primarySample;
    private View rootView;
    private ArrayList<Sample> samples;

    public PlayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_play, container, false);

        // Set the global variables and add listeners to the buttons.
        setVariables();
        setButtonHandlers();
        play();
        return rootView;
    }

    private class GoPlayPauseClickListener implements CompoundButton.OnCheckedChangeListener {
        // Tell the samples to play or pause.
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean pressed) {
            if (pressed) {
                play();
            }
            else {
                pause();
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // Stop playing when the fragment is dismissed
        stop();
        super.onDismiss(dialog);
    }

    private void pause() {
        // Pause all the samples.
        if (isPlaying) {
            for (Sample sample : samples) {
                sample.pause();
            }
            isPlaying = false;
            playPauseButton.setChecked(false);
        }
    }

    private void play() {
        // Play all the samples.
        if (!isPlaying) {
            for (Sample sample : samples) {
                sample.play();
            }
            primarySample.getMediaPlayer().setOnCompletionListener(new PlayCompletedListener());
            isPlaying = true;
            playPauseButton.setChecked(true);
        }
    }

    private class PlayCompletedListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // When the first sample is completed stop all the samples.
            stop();

            // If the user wants to loop start playing again.
            if (isLooping) {
                play();
            }
        }
    }

    private void setButtonHandlers() {
        // Add listeners to the buttons.
        playPauseButton.setOnCheckedChangeListener(new GoPlayPauseClickListener());
        loopToggle.setOnCheckedChangeListener(new ToggleChangeListener());
    }

    private void setSamples(String path) {
        // Put the samples in a list.
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (File file: files) {
            Sample sample = new Sample(file.getAbsolutePath());
            samples.add(sample);
        }
        primarySample = samples.get(0);
    }

    private void setVariables() {
        // Buttons and textview.
        playPauseButton = rootView.findViewById(R.id.buttonPlayPauseRecords);
        loopToggle = rootView.findViewById(R.id.toggleButton);
        TextView textView = rootView.findViewById(R.id.textViewTitle);

        Bundle args = getArguments();

        // Load samples.
        samples = new ArrayList<>();
        String path = args.getString("path");
        setSamples(path);

        // Set title in textview.
        String songTitle = args.getString("songTitle");
        textView.setText(songTitle);

        isPlaying = false;
        isLooping = false;
    }

    private void stop() {
        // Stop playing all the samples.
        if (isPlaying) {
            for (Sample sample : samples) {
                sample.stop();
            }
            isPlaying = false;
            playPauseButton.setChecked(false);
        }
    }

    private class ToggleChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            // Set if the user wants to loop.
            isLooping = b;
        }
    }
}
