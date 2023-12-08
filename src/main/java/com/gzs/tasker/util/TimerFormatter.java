package com.gzs.tasker.util;

public class TimerFormatter {

    private TimerFormatter() {
    }

    public static String formatCountValue(long secondsCount) {
        if (secondsCount < 0) {
            return String.format("-%02d:%02d:%02d", (-secondsCount / 60) / 60, (-secondsCount / 60) % 60, -secondsCount % 60);
        } else {
            return String.format("%02d:%02d:%02d", (secondsCount / 60) / 60, (secondsCount / 60) % 60, secondsCount % 60);
        }
    }
}
