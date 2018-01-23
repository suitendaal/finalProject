package com.example.svenu.loopstation;

/**
 * Created by svenu on 18-1-2018.
 */

public class ActionChecker {

    private int integer;
    public static int notDoing = 0;
    public static int goingToDo = 1;
    public static int doing = 2;

    public ActionChecker(int value) {
        integer = value;
    }

    public int getValue() {
        return integer;
    }

    public void setValue(int value) {
        if (value != integer)
        {
            integer = value;
            signalChanged();
        }
    }

    public void setVariableChangeListener(VariableChangeListener variableChangeListener)
    {
        this.variableChangeListener = variableChangeListener;
    }

    private void signalChanged()
    {
        if (variableChangeListener != null)
            variableChangeListener.onVariableChanged(this);
    }

    public interface VariableChangeListener
    {
        public void onVariableChanged(ActionChecker actionChecker);
    }

    private VariableChangeListener variableChangeListener;
}
