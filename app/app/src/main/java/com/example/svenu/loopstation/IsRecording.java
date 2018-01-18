package com.example.svenu.loopstation;

/**
 * Created by svenu on 18-1-2018.
 */

public class IsRecording {

    private int integer;
    public static int notRecording = 0;
    public static int goingToRecord = 1;
    public static int recording = 2;

    public IsRecording(int value) {
        integer = value;
    }

    public void setValue(int value) {
        if (value != integer)
        {
            integer = value;
            signalChanged();
        }
    }

    public int getValue() {
        return integer;
    }

    public interface VariableChangeListener
    {
        public void onVariableChanged(IsRecording isRecording);
    }

    private VariableChangeListener variableChangeListener;

    public void setVariableChangeListener(VariableChangeListener variableChangeListener)
    {
        this.variableChangeListener = variableChangeListener;
    }

    private void signalChanged()
    {
        if (variableChangeListener != null)
            variableChangeListener.onVariableChanged(this);
    }
}
