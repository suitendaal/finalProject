package com.example.svenu.loopstation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Sven Uitendaal.
 * Class which creates a dialogframe to save a file. It also checks if the filename
 * given by the user is available.
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
        // Function to create an alertdialog.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogMaterialStyle);
        AlertDialogCreator alertDialogCreator = new AlertDialogCreator(loadAlertDialog(alertDialogBuilder));
        alertDialogCreator.setNegativeListener("Cancel");
        alertDialogCreator.setPositiveListener("Ok");
        alertDialogCreator.setButtonClickListener(new GoButtonClickListener());
        alertDialogCreator.create();
    }

    private class GoButtonClickListener implements AlertDialogCreator.ButtonClickListener {
        @Override
        public void onPositiveClick(DialogInterface dialog, int id) {
            // Save the record.

            // Get the right path to save the file.
            String fileName;
            fileName = editText.getText().toString();
            fileName = path + "/" + fileName;

            // Check if this path is available, then save the record.
            if (isFileNameAvailable(fileName)) {
                record.saveRecord(fileName);
                Toast.makeText(context, "Recording saved", Toast.LENGTH_SHORT).show();

                // Delete all of the samples from the sandbox mode, so the user can start over.
                record.delete();
            }
            else {
                Toast.makeText(context, fileName + " not available", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNegativeClick(DialogInterface dialog, int id) {
            // Cancel the dialog.
            dialog.cancel();
        }
    }

    private boolean isFileNameAvailable(String fileName) {
        // Returns true if the path to save the file is legit.
        if (fileName.equals("")) {
            return false;
        }
        else {
            File file = new File(fileName);
            return !file.exists();
        }
    }

    private AlertDialog.Builder loadAlertDialog(AlertDialog.Builder alertDialogBuilder) {
        // Function to create the alertdialog layout.

        RelativeLayout relativeLayout = new RelativeLayout(context);

        // Create an edittext.
        editText = new EditText(context);
        editText.setTextColor(context.getResources().getColor(R.color.colorTextStandard));

        // Set the layout parameters.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        editTextParams.setMargins(128, 0, 128, 0);

        // Add the layout parameters to the layout.
        relativeLayout.setLayoutParams(params);
        relativeLayout.addView(editText, editTextParams);

        // Add the layout to the alertdialog.
        alertDialogBuilder.setTitle("Choose a name");
        alertDialogBuilder.setView(relativeLayout);

        return alertDialogBuilder;
    }
}
