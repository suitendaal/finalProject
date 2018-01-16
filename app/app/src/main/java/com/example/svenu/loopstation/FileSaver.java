package com.example.svenu.loopstation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by svenu on 16-1-2018.
 */

public class FileSaver {

    private Context context;
    private EditText editText;
    private String path;
    private Record record;

    public FileSaver(Context aContext, String aPath, Record aRecord) {
        context = aContext;
        path = aPath;
        record = aRecord;
    }

    public void chooseName() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder = loadAlertDialog(alertDialogBuilder);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                String fileName = editText.getText().toString();
                                boolean isAvailable = isFileNameAvailable(fileName);
                                if (isAvailable) {
                                    createFile(fileName);
                                }
                                else {
                                    Toast.makeText(context, fileName + " not available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createFile(String fileName) {
        //TODO
    }

    private boolean isFileNameAvailable(String fileName) {
        if (fileName.equals("")) {
            return false;
        }
        else {
            File file = new File(path + "/" + fileName + ".mpeg4");
            if (file.exists()) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    private AlertDialog.Builder loadAlertDialog(AlertDialog.Builder alertDialogBuilder) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        editText = new EditText(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        relativeLayout.setLayoutParams(params);
        relativeLayout.addView(editText, editTextParams);

        alertDialogBuilder.setTitle("Choose a name");
        alertDialogBuilder.setView(relativeLayout);

        return alertDialogBuilder;
    }

}
