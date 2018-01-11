package com.example.svenu.loopstation;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by svenu on 10-1-2018.
 */

public class MenuVisibility {
    private Menu menu;
    private boolean searchVisibility;

    public MenuVisibility(Menu aMenu, MenuInflater inflater, boolean aSearchVisibility) {
        menu = aMenu;
        searchVisibility = aSearchVisibility;

        inflater.inflate(R.menu.actions, menu);
        setVisibility();
    }

    private void setVisibility() {
        MenuItem searchItem = menu.findItem(R.id.searchMenu);
        if (searchItem != null) {
            searchItem.setVisible(searchVisibility);
        }
    }
}
