package com.example.svenu.loopstation;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Sven Uitendaal.
 * Class to tell the activity if the search-menuoption should be visible.
 */

public class MenuVisibility {

    public MenuVisibility(Activity activity, Menu menu, MenuInflater inflater) {

        // Inflate the menu.
        inflater.inflate(R.menu.actions, menu);

        // Find out which menuitem to set invisible
        MenuItem menuItem = null;
        if (activity instanceof SearchActivity) {
            menuItem = menu.findItem(R.id.searchMenu);
        } else if (activity instanceof MyRecordingsActivity) {
            menuItem = menu.findItem(R.id.recordingsMenu);
        } else if (activity instanceof AboutActivity) {
            menuItem = menu.findItem(R.id.aboutMenu);
        }

        // Set menuitem invisible.
        if (menuItem != null) {
            menuItem.setVisible(false);
        }
    }
}
