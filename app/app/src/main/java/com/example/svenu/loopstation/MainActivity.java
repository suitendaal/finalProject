package com.example.svenu.loopstation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ToggleButton recordButton;
    private DirectoryCreator directoryCreator;
    private Record record;
    private FileSaver fileSaver;
    private String pathName;
    private String recordDirectory;

    private final String fileFormat = "3gp";
    public static final String appDirectory = "/loopStation";
    public static final String recordingsDirectory = "/myRecordings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        boolean isCover = intent.getBooleanExtra("isCover", false);
        if (isCover) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));
            setLyrics(intent.getStringExtra("url"));
        }
        else {
            getSupportActionBar().setTitle(R.string.sandbox_name);
        }

        requestPermission();
        setVariables();
        createDirectories();
        setButtonHandlers();
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonRecord: {
                    record.buttonClicked();
                    break;
                }
                case R.id.buttonPlay: {
                    record.play();
                    break;
                }
                case R.id.buttonPause: {
                    record.pause();
                    break;
                }
                case R.id.buttonStop: {
                    record.stop();
                    break;
                }
                case R.id.buttonSave: {
                    fileSaver.chooseName();
                    break;
                }
            }
        }
    };

    private void createDirectory(String directoryPath) {
        directoryCreator.createDirectory(directoryPath);
    }

    private void createDirectories() {
        createDirectory(pathName);
        createDirectory(recordDirectory);
        emptyDirectory(pathName, recordDirectory);
    }

    private void emptyDirectory(String pathName, String directoryToKeep) {
        directoryCreator.emptyDirectory(pathName, directoryToKeep);
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
        record.close();
        createDirectories();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createDirectories();
        }
    }

    public void requestPermission() {

        // https://stackoverflow.com/questions/43996635/how-to-grant-android-permission-record-audio-permissions-to-android-shell-user
        String[] requiredPermissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        // If the user previously denied this permission then show a message explaining why
        // this permission is needed
        for (String requiredPermission: requiredPermissions) {
            if (this.checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_GRANTED) {

            } else {
                permissionsToRequest.add(requiredPermission);
            }
        }

        String[] permissionsToRequestArray = new String[permissionsToRequest.size()];
        permissionsToRequestArray = permissionsToRequest.toArray(permissionsToRequestArray);
        if (permissionsToRequestArray.length > 0) {
            requestPermissions(permissionsToRequestArray, 101);
        }
    }

    private void setButtonHandlers() {
        recordButton.setOnClickListener(btnClick);
        findViewById(R.id.buttonPlay).setOnClickListener(btnClick);
        findViewById(R.id.buttonPause).setOnClickListener(btnClick);
        findViewById(R.id.buttonStop).setOnClickListener(btnClick);
        findViewById(R.id.buttonSave).setOnClickListener(btnClick);
    }

    private void setLyrics(String url) {
        TextView lyricTextView = findViewById(R.id.lyricTextView);
        LyricSetter lyricSetter = new LyricSetter(this);
        lyricSetter.setLyrics(url, lyricTextView);
    }

    private void setVariables() {
        pathName = Environment.getExternalStorageDirectory().getAbsolutePath() + appDirectory;
        recordDirectory = pathName + recordingsDirectory;
        directoryCreator = new DirectoryCreator();
        recordButton = findViewById(R.id.buttonRecord);
        record = new Record(this, pathName, fileFormat, recordButton);
        fileSaver = new FileSaver(this, recordDirectory, record);
    }
}
