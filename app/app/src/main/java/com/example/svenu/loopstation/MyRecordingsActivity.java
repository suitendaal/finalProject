package com.example.svenu.loopstation;

import android.content.DialogInterface;
import android.os.Environment;
import android.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
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

/**
 * Class in which the user can see his saved recordings and play them. The saved recordings are
 * loaded in a listview and the user can click them to play them or longclick them to delete them.
 * If a record is not available anymore, when the directory of the recording is empty, the user will
 * be asked if he wants to delete this recording when he clicks it.
 */

public class MyRecordingsActivity extends AppCompatActivity {

    private TextView deleteFileTextView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recordings);

        // Set actionbar title.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(R.string.recordings_name);
        }

        listView = findViewById(R.id.recordingsListView);
        setListViewHandlers();
        loadFilesInListView();
    }

    private boolean deleteRecording(String path) {
        // Function to delete a recording.

        File recordToDelete = new File(path);
        if (recordToDelete.isDirectory())
        {
            // Delete all of the samples in the directory.
            String[] children = recordToDelete.list();
            for (String child : children) {
                new File(recordToDelete, child).delete();
            }
        }

        // Delete the empty directory.
        return recordToDelete.delete();
    }

    private class GoAlertButtonClickListener implements AlertDialogCreator.ButtonClickListener {
        @Override
        public void onPositiveClick(DialogInterface dialog, int id) {
            // Delete the selected recording.
            boolean deleted = deleteRecording(deleteFileTextView.getTag().toString());
            Log.d("Deleted recording", deleted + "");
            if (deleted) {
                Toast.makeText(MyRecordingsActivity.this, "Deleted " + deleteFileTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                loadFilesInListView();
            }
            else {
                Toast.makeText(MyRecordingsActivity.this, "Attempt to delete " + deleteFileTextView.getText().toString() + " failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNegativeClick(DialogInterface dialog, int id) {
            // Close the dialog.
            dialog.cancel();
        }
    }

    private AlertDialog.Builder loadAlertDialog(String fileName) {
        // Function to create an alertdialogbuilder.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogMaterialStyle);
        alertDialogBuilder.setTitle("Do you want to delete " + fileName + "?");
        return alertDialogBuilder;
    }

    private void loadFilesInListView() {
        // Function to show the saved recordings in a listview.

        // Store the filepaths in a list.
        // https://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + MainActivity.appDirectory + MainActivity.recordingsDirectory;
        File directory = new File(path);
        File[] files = directory.listFiles();

        // Load the list in the listview.
        ArrayList<File> recordings = new ArrayList<>(Arrays.asList(files));
        RecordingsAdapter recordingsAdapter = new RecordingsAdapter(this, recordings);
        listView.setAdapter(recordingsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Load the menu with the right menuoptions.
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();

        // Tell the menu which menuoptions must be visible.
        new MenuVisibility(this, menu, inflater);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // When an option is clicked, load a new activity.
        MenuOption menuOption = new MenuOption(item);
        menuOption.loadActivity(this);
        return true;
    }

    private boolean playRecording(String path) {
        // Function to play the recording in a fragment.

        // Get all of the samples.
        File directory = new File(path);
        File[] files = directory.listFiles();

        // Ensure the selected directory is not empty.
        if (files.length > 0) {
            // Start the fragment with the right song.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            PlayFragment playFragment = new PlayFragment();
            Bundle args = new Bundle();
            args.putString("path", path);
            args.putString("songTitle", directory.getName());
            playFragment.setArguments(args);
            playFragment.show(ft, "play file");
            return true;
        }

        // If no samples are available the record is not available.
        else {
            Toast.makeText(this, "Record not available", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private ListView.OnItemClickListener recordingsClick = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // Play the recording.
            TextView fileTextView = view.findViewById(R.id.textViewFile);
            String path = fileTextView.getTag().toString();

            // If recording is not available ask to delete the recording.
            if (!playRecording(path)) {
                recordingsLongClick.onItemLongClick(adapterView, view, i, l);
            }
        }
    };

    private ListView.OnItemLongClickListener recordingsLongClick = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            // Ask to delete the recording in an alertdialog.
            deleteFileTextView = view.findViewById(R.id.textViewFile);
            AlertDialog.Builder alertDialogBuilder = loadAlertDialog(deleteFileTextView.getText().toString());
            AlertDialogCreator alertDialogCreator = new AlertDialogCreator(alertDialogBuilder);
            alertDialogCreator.setPositiveListener("Yes");
            alertDialogCreator.setNegativeListener("No");
            alertDialogCreator.create();
            alertDialogCreator.setButtonClickListener(new GoAlertButtonClickListener());
            return true;
        }
    };

    private void setListViewHandlers() {
        // Set listeners to listview click.
        listView.setOnItemClickListener(recordingsClick);
        listView.setOnItemLongClickListener(recordingsLongClick);
    }
}
