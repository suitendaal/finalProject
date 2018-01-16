package com.example.svenu.loopstation;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String pathname;
    String recordDirectory;
    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        boolean isCover = intent.getBooleanExtra("isCover", false);
        if (isCover) {
            String title = intent.getStringExtra("title");
            getSupportActionBar().setTitle(title);
            String url = intent.getStringExtra("url");
            setLyrics(url);
        }
        else {
            getSupportActionBar().setTitle(R.string.sandbox_name);
        }

        // https://stackoverflow.com/questions/43996635/how-to-grant-android-permission-record-audio-permissions-to-android-shell-user
        requestPermission();

        pathname = Environment.getExternalStorageDirectory().getAbsolutePath()+"/loopStation";
        recordDirectory = pathname + "/myRecordings";

        // https://stackoverflow.com/questions/17794974/create-folder-in-android
        createDirectory(pathname);
        createDirectory(recordDirectory);

        // https://stackoverflow.com/questions/4943629/how-to-delete-a-whole-folder-and-content
        emptyDirectory();

        record = new Record(pathname);

        // https://stackoverflow.com/questions/8499042/android-audiorecord-example
        setButtonHandlers();
    }

    private void emptyDirectory() {
        File dir = new File(pathname);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                File file = new File(dir, children[i]);
                String fileName = file.getAbsolutePath();
                if (!fileName.equals(recordDirectory)){
                    Log.d("FileToDelete", fileName);
                    file.delete();
                }
            }
        }
    }

    public void requestPermission() {

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // This method is called when the permissions are given
        }
    }

    private void createDirectory(String directoryPath) {
        Log.d("directoryPath", directoryPath);
        File folder = new File(directoryPath);
        boolean success = true;
        if (!folder.exists()) {
            Log.d("directory", "mkdir");
            success = folder.mkdirs();
        }
        if (success) {
            Log.d("directory", "created");
        } else {
            Log.d("directory", "failed");
        }
    }

    private void setButtonHandlers() {
        ((ToggleButton) findViewById(R.id.buttonRecord)).setOnCheckedChangeListener(checkChange);
        ((Button) findViewById(R.id.buttonPlay)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.buttonPause)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.buttonStop)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.buttonSave)).setOnClickListener(btnClick);
    }

    private ToggleButton.OnCheckedChangeListener checkChange = new ToggleButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                record.startRecording();
            }
            else {
                record.stopRecording();
            }
        }
    };

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonPlay: {
                    // TODO
                    break;
                }
                case R.id.buttonPause: {
                    //TODO
                    break;
                }
                case R.id.buttonStop: {
                    //TODO
                    break;
                }
                case R.id.buttonSave: {
                    saveRecord();
                    break;
                }
            }
        }
    };

    private void saveRecord() {
        FileSaver fileSaver = new FileSaver(this, recordDirectory, record);
        fileSaver.chooseName();
    }

    // Code for lyrics
    private void setLyrics(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        TextView lyricTextView = findViewById(R.id.lyricTextView);
                        String lyrics = responseToLyrics(response);
                        lyricTextView.setText(lyrics);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private String responseToLyrics(String response) {
        String lyrics = response.split("<div class=\"lyrics\">\n")[1];

        String div = "<div";
        int divLen = div.length();
        String endDiv = "</div";
        int endDivLen = endDiv.length();
        int divCount = 1;
        int endDivCount = 0;

        int lyricsLength = lyrics.length();
        int lyricsIndex = 0;
        while (lyricsIndex < lyricsLength - endDivLen) {
            if (lyrics.substring(lyricsIndex, lyricsIndex + divLen).equals(div)) {
                divCount += 1;
            }
            else if (lyrics.substring(lyricsIndex, lyricsIndex + endDivLen).equals(endDiv)) {
                endDivCount += 1;
            }
            if (divCount == endDivCount) {
                lyrics = lyrics.substring(0, lyricsIndex);
                break;
            }
            lyricsIndex += 1;
        }
        lyrics = lyrics.replaceAll("(?s)<.*?>", "");

        return lyrics;
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
