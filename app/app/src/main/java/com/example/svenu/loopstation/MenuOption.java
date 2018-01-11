package com.example.svenu.loopstation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by svenu on 10-1-2018.
 */

public class MenuOption {
    private MenuItem menuItem;
    private Context context;

    public MenuOption(MenuItem aMenuItem) {
        Log.d("MenuOption", "created");
        menuItem = aMenuItem;
    }

    public void loadActivity(Context aContext) {
        context = aContext;
        switch (menuItem.getItemId()) {
            case R.id.searchMenu:
                loadNewActivity(SearchActivity.class);
                break;
            case R.id.sandboxMenu:
                loadNewActivity(MainActivity.class, false);
                break;
            case R.id.recordingsMenu:
                //TODO
                break;
            case R.id.aboutMenu:
                loadNewActivity(AboutActivity.class);
                break;
        }
    }

    private void loadNewActivity(Class myClass) {
        Intent intent = new Intent(context, myClass);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private void loadNewActivity(Class myClass, boolean isCover) {
        Intent intent = new Intent(context, myClass);
        intent.putExtra("isCover", isCover);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
