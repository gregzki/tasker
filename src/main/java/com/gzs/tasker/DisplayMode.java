package com.gzs.tasker;

import java.util.HashSet;
import java.util.Set;

public class DisplayMode {

    private final Set<Display> displays = new HashSet<>();
    private Value value = Value.TODAY;

    public void registerDisplay(Display display) {
        displays.add(display);
    }

    public void setValue(Value value) {
        this.value = value;
        displays.forEach(d -> d.refresh(value));
    }

    public Value getValue() {
        return value;
    }

    public enum Value {
        TODAY,
        FULL,
        LAST_RUN
    }
}
