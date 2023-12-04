package com.gzs.tasker;

public class DisplayMode {
    private Value value = Value.TODAY;

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
    public enum Value {
        TODAY,
        FULL,
        RECENT
    }
}
