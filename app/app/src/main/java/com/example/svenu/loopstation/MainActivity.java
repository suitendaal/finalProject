package com.example.svenu.loopstation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by Sven Uitendaal.
 * Activity where the user can record samples and listen to them. The user can also see lyrics of a
 * song when he searched for it. At the end the user can save the recorded samples.
 */

public class MainActivity extends AppCompatActivity {

    private DirectoryCreator directoryCreator;
    private FileSaver fileSaver;
    private ImageView iconView;
    private String pathName;
    private ToggleButton playPauseButton;
    private Record record;
    private ToggleButton recordButton;
    private String recordDirectory;
    private View scrollView;

    private final String fileFormat = "m4a";
    public static final String appDirectory = "/loopStation";
    public static final String recordingsDirectory = "/myRecordings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVariables();
        Intent intent = getIntent();

        // If activity is started via the Search activity isCover equals true.
        boolean isCover = intent.getBooleanExtra("isCover", false);
        ActionBar actionBar = getSupportActionBar();

        if (isCover) {
            // Set actionbar title as song name and load lyrics.
            if (actionBar != null) {
                getSupportActionBar().setTitle(intent.getStringExtra("title"));
            }
            setLyrics(intent.getStringExtra("url"));
        }
        else {
            // Set actionbar title and load the icon_squared as background.
            if (actionBar != null){
                getSupportActionBar().setTitle(R.string.sandbox_name);
            }
        }
        // Show lyrics or icon.
        setBackGround(isCover);

        // When the app is opened for the first time, ask for permissions
        requestPermission();

        // If the directories the app uses doesn't exist yet, create them.
        createDirectories();

        // Add functions to the buttons.
        setButtonHandlers();
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        // Functions when a button is clicked.

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonRecord: {
                    // Tell the record the button is clicked.
                    record.buttonRecordClicked();
                    break;
                }
                case R.id.buttonPlayPauseMain: {
                    // Tell the record the button is clicked.
                    record.buttonPlayPauseClicked();
                    break;
                }
                case R.id.buttonStop: {
                    // Stop playing the recorded samples.
                    record.stop();
                    break;
                }
                case R.id.buttonSave: {
                    // When samples are recorded, save them.
                    if (record.getSampleSize() > 0) {
                        record.pause();
                        fileSaver.chooseName();
                    }
                    else {
                        // When no samples are recorded it is not possible to save.
                        Toast.makeText(MainActivity.this, "No samples recorded", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    private void createDirectory(String directoryPath) {
        // Create a directory with a given path.
        directoryCreator.createDirectory(directoryPath);
    }

    private void createDirectories() {
        // Create directories the app uses.
        createDirectory(pathName);
        createDirectory(recordDirectory);

        // Empty the main directory, except for recordDirectory.
        emptyDirectory(pathName, recordDirectory);
    }

    private void emptyDirectory(String pathName, String directoryToKeep) {
        // Empty a directory and keep one given directory.
        directoryCreator.emptyDirectory(pathName, directoryToKeep);
    }

    private View.OnLongClickListener iconClicked = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            // Easter egg: show who created the icon.
            Toast.makeText(MainActivity.this, getResources().getString(R.string.icon_toast), Toast.LENGTH_SHORT).show();
            return false;
        }
    };

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

        // Close and delete the record.
        record.close();
        record.delete();

        // Ensure the directories still exists, that the user didn't do anything stupid.
        createDirectories();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        // If permissions are given, create the directories.
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createDirectories();
        }
    }

    public void requestPermission() {
        // Function to request permission when they are not given yet.

        // https://stackoverflow.com/questions/43996635/how-to-grant-android-permission-record-audio-permissions-to-android-shell-user
        String[] requiredPermissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        // If the user previously denied this permission then show a message explaining why
        // this permission is needed
        for (String requiredPermission: requiredPermissions) {
            if (this.checkCallingOrSelfPermission(requiredPermission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(requiredPermission);
            }
        }

        String[] permissionsToRequestArray = new String[permissionsToRequest.size()];
        permissionsToRequestArray = permissionsToRequest.toArray(permissionsToRequestArray);
        if (permissionsToRequestArray.length > 0) {
            requestPermissions(permissionsToRequestArray, 101);
        }
    }

    private void setBackGround(boolean isCover) {
        // Sets the background as lyrics or as icon.
        if (isCover) {
            iconView.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
        else {
            iconView.setImageResource(R.drawable.icon_shape);
            iconView.setOnLongClickListener(iconClicked);
        }
    }

    private void setButtonHandlers() {
        // Add a listener to all of the buttons.
        recordButton.setOnClickListener(btnClick);
        playPauseButton.setOnClickListener(btnClick);
        findViewById(R.id.buttonStop).setOnClickListener(btnClick);
        findViewById(R.id.buttonSave).setOnClickListener(btnClick);
    }

    private void setLyrics(String url) {
        // Function to load the lyrics from html to text.
        TextView lyricTextView = findViewById(R.id.lyricTextView);
        LyricSetter lyricSetter = new LyricSetter(this);
        lyricSetter.setLyrics(url, lyricTextView);
    }

    private void setVariables() {
        // Function to set al the global variables.
        pathName = Environment.getExternalStorageDirectory().getAbsolutePath() + appDirectory;
        recordDirectory = pathName + recordingsDirectory;
        directoryCreator = new DirectoryCreator();
        iconView = findViewById(R.id.iconView);
        scrollView = findViewById(R.id.lyricScrollView);
        recordButton = findViewById(R.id.buttonRecord);
        playPauseButton = findViewById(R.id.buttonPlayPauseMain);
        record = new Record(pathName, fileFormat, recordButton, playPauseButton);
        fileSaver = new FileSaver(this, recordDirectory, record);
    }

    @Override
    protected void onStop() {
        // When the app stops, close the record.
        record.close();
        super.onStop();
    }
}
