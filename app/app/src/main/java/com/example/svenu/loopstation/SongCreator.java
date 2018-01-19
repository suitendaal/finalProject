package com.example.svenu.loopstation;

import android.app.Activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by svenu on 18-1-2018.
 */

public class SongCreator {
    
    Activity activity;

    public SongCreator(Activity anActivity) {
        activity = anActivity;
    }

    public void createSong(String fileName, ArrayList<File> files) {
        ArrayList<byte[]> songs = new ArrayList<>();
        for (File file: files) {
           songs.add(fileToBytes(file));
        }

        ArrayList<Integer> songLengths = calculateSongLengths(songs);
        byte[] newSong = new byte[songLengths.get(0)];
        for (int i = 0; i < songLengths.get(0); i++) {
            int newByteValue = 0;
            int numberOfSongsParticipating = 0;
            for (int j = 0; j < songs.size(); j++) {
                if (i < songLengths.get(j)) {
                    newByteValue += songs.get(j)[i];
                    numberOfSongsParticipating += 1;
                }
            }
            if (numberOfSongsParticipating > 0) {
                newByteValue = newByteValue / numberOfSongsParticipating;
            }
            else {
                newByteValue = 0;
            }
            byte newByte = (byte) newByteValue;
            newSong[i] = newByte;
        }

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(newSong);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Integer> calculateSongLengths(ArrayList<byte[]> songs) {
        ArrayList<Integer> songLengths = new ArrayList<>();
        for (byte[] song: songs) {
            songLengths.add(song.length);
        }
        return songLengths;
    }

    private byte[] fileToBytes(File file) {
        // https://stackoverflow.com/questions/21305735/convert-large-3gp-files-into-byte-array

        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while (fis.available() > 0) {
                bos.write(fis.read());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        byte[] bytes = bos.toByteArray();

        return bytes;
    }
}
