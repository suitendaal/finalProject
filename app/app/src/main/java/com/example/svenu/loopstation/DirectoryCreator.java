package com.example.svenu.loopstation;

import android.util.Log;

import java.io.File;

/**
 * Created by svenu on 17-1-2018.
 */

public class DirectoryCreator {

    public DirectoryCreator() {}

    public void emptyDirectory(String pathName, String directoryToKeep) {

        // https://stackoverflow.com/questions/4943629/how-to-delete-a-whole-folder-and-content
        File dir = new File(pathName);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                File file = new File(dir, children[i]);
                String fileName = file.getAbsolutePath();
                if (!fileName.equals(directoryToKeep)){
                    Log.d("FileToDelete", fileName);
                    file.delete();
                }
            }
        }
    }

    public void createDirectory(String directoryPath) {

        // https://stackoverflow.com/questions/17794974/create-folder-in-android
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
}
