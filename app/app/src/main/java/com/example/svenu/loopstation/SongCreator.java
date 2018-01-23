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

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

/**
 * Created by svenu on 18-1-2018.
 */

public class SongCreator {

    Activity activity;

    public SongCreator(Activity anActivity) {
        activity = anActivity;
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
