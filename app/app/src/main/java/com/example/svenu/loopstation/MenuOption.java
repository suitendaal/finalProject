package com.example.svenu.loopstation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

/**
 * Class to tell the app which activity to load when a menuoption is clicked.
 */

public class MenuOption {

    private Context context;
    private MenuItem menuItem;

    public MenuOption(MenuItem aMenuItem) {
        menuItem = aMenuItem;
    }

    public void loadActivity(Context aContext) {
        // Function to check which activity to load.
        context = aContext;
        switch (menuItem.getItemId()) {
            case R.id.searchMenu:
                // Load a searchactivity
                loadNewActivity(SearchActivity.class);
                break;
            case R.id.sandboxMenu:
                // Load a mainactivity
                loadNewActivity(MainActivity.class, false);
                break;
            case R.id.recordingsMenu:
                // Load a myrecordingsactivity
                loadNewActivity(MyRecordingsActivity.class);
                break;
            case R.id.aboutMenu:
                // Load an aboutactivity
                loadNewActivity(AboutActivity.class);
                break;
        }
    }

    private void loadNewActivity(Class myClass) {
        // Function to load a new activity.
        Intent intent = new Intent(context, myClass);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private void loadNewActivity(Class myClass, boolean isCover) {
        // Function to load a new activity with an extra boolean.
        Intent intent = new Intent(context, myClass);
        intent.putExtra("isCover", isCover);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
