package com.example.svenu.loopstation;

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
 * Created by svenu on 11-1-2018.
 */

public class SongAdapter extends ArrayAdapter {
    private ArrayList<Song> songs;
    private Context context;

    public SongAdapter(Context aContext, ArrayList<Song> aSongs) {
        super(aContext, R.layout.row_search, aSongs);
        songs = aSongs;
        context = aContext;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.row_search, parent, false);

        TextView titleTextView = rootView.findViewById(R.id.textViewSongTitle);
        TextView artistTextView = rootView.findViewById(R.id.textViewSongArtist);

        Song song = songs.get(position);
        titleTextView.setText(song.getTitle());
        titleTextView.setTag(song.getUrl());
        artistTextView.setText(song.getArtist());

        return rootView;
    }
}
