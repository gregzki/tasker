package com.gzs.tasker;

import java.util.HashSet;
import java.util.Set;

public class DisplayImpl {

    private final Set<Display> displays = new HashSet<>();
    private Mode mode = Mode.TODAY;

    public void registerDisplay(Display display) {
        displays.add(display);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        displays.forEach(Display::refresh);
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        TODAY,
        FULL,
        LAST_RUN
    }
}
