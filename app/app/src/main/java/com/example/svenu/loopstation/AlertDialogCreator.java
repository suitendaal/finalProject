package com.example.svenu.loopstation;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by Sven Uitendaal.
 * Class to create an alertdialog with a positive and a negative.
 */

public class AlertDialogCreator {

    private AlertDialog.Builder alertDialogBuilder;
    private ButtonClickListener buttonClickListener;

    public AlertDialogCreator(AlertDialog.Builder anAlertDialogBuilder) {
        alertDialogBuilder = anAlertDialogBuilder;
        alertDialogBuilder.setCancelable(false);
    }

    public interface ButtonClickListener {
        void onPositiveClick(DialogInterface dialog, int id);
        void onNegativeClick(DialogInterface dialog, int id);
    }

    public void create() {
        // Creates an alertdialog.
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setButtonClickListener(ButtonClickListener aButtonClickListener) {
        // Sets the listener.
        buttonClickListener = aButtonClickListener;
    }

    public void setPositiveListener(String positiveText) {
        // Add a positive button clicklistener.
        alertDialogBuilder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                                buttonClickListener.onPositiveClick(dialog, id);
                        }
                    });
    }

    public void setNegativeListener(String negativeText) {
        // Add a negative button clicklistener.
        alertDialogBuilder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        buttonClickListener.onNegativeClick(dialog, id);
                    }
                });
    }
}
