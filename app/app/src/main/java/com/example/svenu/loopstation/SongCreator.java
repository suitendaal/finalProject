package com.example.svenu.loopstation;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by svenu on 18-1-2018.
 */

public class SongCreator {

    public SongCreator() {
    }

    public void createSong(String fileName, ArrayList<File> files) throws IOException {
        DirectoryCreator directoryCreator = new DirectoryCreator();
        directoryCreator.createDirectory(fileName);

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
