package com.gzs.tasker.report;

public class DataRow {
    private String day;
    private String task;
    private String time;
    private long switchCount;

    public DataRow() {
    }

    public DataRow(String day, String task, String time, long switchCount) {
        this.day = day;
        this.task = task;
        this.time = time;
        this.switchCount = switchCount;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getSwitchCount() {
        return switchCount;
    }

    public void setSwitchCount(long switchCount) {
        this.switchCount = switchCount;
    }
}
