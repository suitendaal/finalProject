package com.example.svenu.loopstation;

import android.content.DialogInterface;
import android.os.Environment;
import android.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MyRecordingsActivity extends AppCompatActivity {

    private ListView listView;
    private TextView fileTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recordings);

        getSupportActionBar().setTitle(R.string.recordings_name);
        listView = findViewById(R.id.recordingsListView);

        setListViewHandlers();
        loadFilesInListView();
    }

    private void deleteRecording(String path) {
        File recordToDelete = new File(path);
        if (recordToDelete.isDirectory())
        {
            String[] children = recordToDelete.list();
            for (int j = 0; j < children.length; j++)
            {
                new File(recordToDelete, children[j]).delete();
            }
        }
        boolean deleted = recordToDelete.delete();
        Log.d("Delete recording", "deleted: " + deleted);
    }

    private class GoAlertButtonClickListener implements AlertDialogCreator.ButtonClickListener {
        @Override
        public void onPositiveClick(DialogInterface dialog, int id) {
            deleteRecording(fileTextView.getTag().toString());
            Toast.makeText(getApplicationContext(), "Deleted " + fileTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            loadFilesInListView();
        }

        @Override
        public void onNegativeClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }

        @Override
        public void onNeutralClick(DialogInterface dialog, int id) {

        }
    }

    private AlertDialog.Builder loadAlertDialog(String fileName) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        alertDialogBuilder.setTitle("Do you want to delete " + fileName + "?");
        return alertDialogBuilder;
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

    private boolean playRecording(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files.length > 0) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            PlayFragment playFragment = new PlayFragment();
            Bundle args = new Bundle();
            args.putString("path", path);
            args.putString("songTitle", directory.getName());
            playFragment.setArguments(args);
            playFragment.show(ft, "play file");
            return true;
        }
        else {
            Toast.makeText(this, "Record not available", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private ListView.OnItemClickListener recordingsClick = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            fileTextView = view.findViewById(R.id.textViewFile);
            String path = fileTextView.getTag().toString();
            if (!playRecording(path)) {
                recordingsLongClick.onItemLongClick(adapterView, view, i, l);
            }

        }
    };

    private ListView.OnItemLongClickListener recordingsLongClick = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            TextView fileTextView = view.findViewById(R.id.textViewFile);
            AlertDialog.Builder alertDialogBuilder = loadAlertDialog(fileTextView.getText().toString());
            AlertDialogCreator alertDialogCreator = new AlertDialogCreator(alertDialogBuilder);
            alertDialogCreator.setPositiveListener("Yes");
            alertDialogCreator.setNegativeListener("No");
            alertDialogCreator.create();
            alertDialogCreator.setButtonClickListener(new GoAlertButtonClickListener());

            return true;
        }
    };

    private void setListViewHandlers() {
        listView.setOnItemClickListener(recordingsClick);
        listView.setOnItemLongClickListener(recordingsLongClick);
    }
}
