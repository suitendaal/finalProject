package com.example.svenu.loopstation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;

/**
 * Class which creates a dialogframe to save a file. It also checks if the filename
 * given by the user is available.
 */

public class FileSaver {

    private Context context;
    private EditText editText;
    private String path;
    private Record record;
    private boolean isNameAvailable;

    public FileSaver(Context aContext, String aPath, Record aRecord) {
        context = aContext;
        path = aPath;
        record = aRecord;
        isNameAvailable = false;
    }

    public void chooseName() {
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
            String fileName = editText.getText().toString();
            fileName = path + "/" + fileName;
            Log.d("fileName", fileName);
            isNameAvailable = isFileNameAvailable(fileName);
            if (isNameAvailable) {
                record.saveRecord(fileName);
                Toast.makeText(context, "Recording saved", Toast.LENGTH_SHORT).show();
                record.delete();
            }
            else {
                Toast.makeText(context, fileName + " not available", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNegativeClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }

        @Override
        public void onNeutralClick(DialogInterface dialog, int id) {
        }
    }

    private AlertDialog.Builder loadAlertDialog(AlertDialog.Builder alertDialogBuilder) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        editText = new EditText(context);
        editText.setTextColor(context.getResources().getColor(R.color.colorTextStandard));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        editTextParams.setMargins(128, 0, 128, 0);

        relativeLayout.setLayoutParams(params);
        relativeLayout.addView(editText, editTextParams);

        alertDialogBuilder.setTitle("Choose a name");
        alertDialogBuilder.setView(relativeLayout);

        return alertDialogBuilder;
    }

    private boolean isFileNameAvailable(String fileName) {
        if (fileName.equals("")) {
            return false;
        }
        else {
            File file = new File(fileName);
            return !file.exists();
        }
    }
}
