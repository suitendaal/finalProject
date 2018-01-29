package com.example.svenu.loopstation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Adapter to show recordings in a listview.
 */

public class RecordingsAdapter extends ArrayAdapter {
    private ArrayList<File> recordings;
    private Context context;

    public RecordingsAdapter(Context aContext, ArrayList<File> aRecordings) {
        super(aContext, R.layout.row_recording, aRecordings);
        recordings = aRecordings;
        context = aContext;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Inflate the rootview
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.row_recording, parent, false);

        // Initialize textview and data.
        TextView fileTitleTextView = rootView.findViewById(R.id.textViewFile);
        File recording = recordings.get(position);

        // Set the texts and tags.
        fileTitleTextView.setText(recording.getName());
        fileTitleTextView.setTag(recording.getAbsolutePath());

        return rootView;
    }
}
