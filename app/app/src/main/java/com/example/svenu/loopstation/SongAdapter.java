package com.example.svenu.loopstation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter to show searched songs in the searchactivity in a listview.
 */

public class SongAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Song> songs;

    public SongAdapter(Context aContext, ArrayList<Song> aSongs) {
        super(aContext, R.layout.row_search, aSongs);
        context = aContext;
        songs = aSongs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Inflate the rootview.
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.row_search, parent, false);

        // Initialize the textviews.
        TextView titleTextView = rootView.findViewById(R.id.textViewSongTitle);
        TextView artistTextView = rootView.findViewById(R.id.textViewSongArtist);

        // Set the texts and tags.
        Song song = songs.get(position);
        titleTextView.setText(song.getTitle());
        titleTextView.setTag(song.getUrl());
        artistTextView.setText(song.getArtist());

        return rootView;
    }
}
