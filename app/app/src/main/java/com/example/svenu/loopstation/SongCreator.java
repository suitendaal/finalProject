package com.example.svenu.loopstation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sven Uitendaal.
 * Class which copies all the recorded samples to another directory. It saves the recorded song.
 */

public class SongCreator {

    public SongCreator() {
    }

    public void createSong(String fileName, ArrayList<File> files) throws IOException {

        // Create a new directory in the given path.
        DirectoryCreator directoryCreator = new DirectoryCreator();
        directoryCreator.createDirectory(fileName);

        // Copy every sample to this new created directory.
        for (File file : files) {
            String pathName = fileName + "/" + file.getName();
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(pathName));
            byte[] buf = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, len);
            }
            fileInputStream.close();
            fileOutputStream.close();
        }
    }
}
