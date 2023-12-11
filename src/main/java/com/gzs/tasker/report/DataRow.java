package com.gzs.tasker.report;

public class DataRow {
    private String day;
    private String task;
    private String time;

    public DataRow() {
    }

    public DataRow(String day, String task, String time) {
        this.day = day;
        this.task = task;
        this.time = time;
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
}
