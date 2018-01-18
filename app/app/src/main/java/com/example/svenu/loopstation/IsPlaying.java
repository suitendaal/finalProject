package com.example.svenu.loopstation;

/**
 * Created by svenu on 18-1-2018.
 */

public class IsPlaying {

    private boolean aBoolean;

    public IsPlaying(boolean value) {
        aBoolean = value;
    }

    public void setValue(boolean value) {
        if (value != aBoolean)
        {
            aBoolean = value;
            signalChanged();
        }
    }

    public boolean getValue() {
        return aBoolean;
    }

    public interface VariableChangeListener
    {
        public void onVariableChanged(IsPlaying isPlaying);
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
