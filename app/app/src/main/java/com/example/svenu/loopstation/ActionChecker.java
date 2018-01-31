package com.example.svenu.loopstation;

/**
 * Created by Sven Uitendaal.
 * This class is a kind of boolean but with 3 states; doing, going to do and not doing.
 * It also has a listener to check when this state changes.
 */

public class ActionChecker {

    public static int notDoing = 0;
    public static int goingToDo = 1;
    public static int doing = 2;

    private int state;
    private VariableChangeListener variableChangeListener;

    public ActionChecker(int value) {
        state = value;
    }

    public int getValue() {
        // Returns the state.

        return state;
    }

    public void setValue(int value) {
        // Sets the state.

        if (value != state)
        {
            state = value;

            // Let the listener know the state changed.
            signalChanged();
        }
    }

    public void setVariableChangeListener(VariableChangeListener aVariableChangeListener) {
        // Sets the listener.
        variableChangeListener = aVariableChangeListener;
    }

    private void signalChanged() {
        // If the listener is set, call the listener functions
        if (variableChangeListener != null)
            variableChangeListener.onVariableChanged(this);
    }

    public interface VariableChangeListener {
        void onVariableChanged(ActionChecker actionChecker);
    }
}
