package com.example.svenu.loopstation;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MyRecordingsActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recordings);

        getSupportActionBar().setTitle(R.string.recordings_name);
        listView = findViewById(R.id.recordingsListView);

        setListViewHandlers();
        loadFilesInListView();
    }

    private void setListViewHandlers() {
        listView.setOnItemClickListener(recordingsClick);
        listView.setOnItemLongClickListener(recordingsLongClick);
    }

    private ListView.OnItemClickListener recordingsClick = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView fileTextView = view.findViewById(R.id.textViewFile);
            String path = fileTextView.getTag().toString();
            playRecording(path);
        }
    };

    private ListView.OnItemLongClickListener recordingsLongClick = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView fileTextView = view.findViewById(R.id.textViewFile);
            String path = fileTextView.getTag().toString();
            File recordToDelete = new File(path);
            boolean deleted = recordToDelete.delete();
            Log.d("Delete recording", "deleted: " + deleted);
            loadFilesInListView();

            return true;
        }
    };

    private void playRecording(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (File file: files) {
            Sample sample = new Sample(file.getAbsolutePath());
            sample.play();
        }
    }

    private void loadFilesInListView() {

        // https://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + MainActivity.appDirectory + MainActivity.recordingsDirectory;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);

        ArrayList<File> recordings = new ArrayList<>(Arrays.asList(files));
        RecordingsAdapter recordingsAdapter = new RecordingsAdapter(this, recordings);
        listView.setAdapter(recordingsAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();

        new MenuVisibility(menu, inflater, true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        MenuOption menuOption = new MenuOption(item);
        menuOption.loadActivity(this);
        return true;
    }
}
