package com.example.svenu.loopstation;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by svenu on 23-1-2018.
 */

public class AlertDialogCreator {

    private AlertDialog.Builder alertDialogBuilder;
    private ButtonClickListener buttonClickListener;

    public AlertDialogCreator(AlertDialog.Builder anAlertDialogBuilder) {
        alertDialogBuilder = anAlertDialogBuilder;
        alertDialogBuilder.setCancelable(false);
    }

    public interface ButtonClickListener {
        public void onPositiveClick(DialogInterface dialog, int id);

        public void onNegativeClick(DialogInterface dialog, int id);

        public void onNeutralClick(DialogInterface dialog, int id);
    }

    public void create() {
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setButtonClickListener(ButtonClickListener aButtonClickListener) {
        buttonClickListener = aButtonClickListener;
    }

    public void setPositiveListener(String positiveText) {
        alertDialogBuilder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                                buttonClickListener.onPositiveClick(dialog, id);
                        }
                    });
    }

    public void setNegativeListener(String negativeText) {
        alertDialogBuilder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        buttonClickListener.onNegativeClick(dialog, id);
                    }
                });
    }

    public void setNeutralListener(String neutralText) {
        alertDialogBuilder.setNeutralButton(neutralText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        buttonClickListener.onPositiveClick(dialog, id);
                    }
                });
    }
}
