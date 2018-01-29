package com.example.svenu.loopstation;

import android.util.Log;

import java.io.File;

/**
 * Class to create a new directory on the mobile device.
 */

public class DirectoryCreator {

    public DirectoryCreator() {}

    public void createDirectory(String directoryPath) {
        // Function to create a new directory on the mobile device.

        // https://stackoverflow.com/questions/17794974/create-folder-in-android
        Log.d("directoryPath", directoryPath);
        File folder = new File(directoryPath);

        // Create the directory if it doesn't exist yet.
        boolean success = false;
        if (!folder.exists()) {
            Log.d("directory", "mkdir");
            success = folder.mkdirs();
        }
        Log.d("directory created", success + "");
    }

    public void emptyDirectory(String pathName, String directoryToKeep) {
        // Function to delete all files in a directory, except for one file the user wants to keep.

        // https://stackoverflow.com/questions/4943629/how-to-delete-a-whole-folder-and-content
        File dir = new File(pathName);
        if (dir.isDirectory())
        {
            String[] children = dir.list();

            // Delete all of the files in the directory, except the directory the user wants to keep.
            for (String child : children) {
                File file = new File(dir, child);
                String fileName = file.getAbsolutePath();
                if (!fileName.equals(directoryToKeep)) {

                    // First empty the directory if the file is a directory.
                    emptyDirectory(fileName, directoryToKeep);
                    file.delete();
                }
            }
        }
    }
}
