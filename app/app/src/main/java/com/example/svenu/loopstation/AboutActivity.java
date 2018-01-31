package com.example.svenu.loopstation;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Sven Uitendaal.
 * This activity exists to tell the user who created this application and
 * especially how to use this app.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set the title of this activity.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(R.string.about_name);
        }

        // Load the 'About' text.
        TextView textView = findViewById(R.id.textViewAbout);
        textView.setText(R.string.instructions);
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
}
