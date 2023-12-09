package com.gzs.tasker.state;

import java.util.ArrayList;
import java.util.List;

public class State {
    List<Task> tasks = new ArrayList<>();

    public Task addTask(String title) {
        Task task = new Task(title);
        tasks.add(task);
        return task;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getActiveTasks() {
        return tasks.stream()
                .filter(t -> !t.isArchived())
                .toList();
    }

    public void cleanUp() {
        tasks.removeIf(task ->
                task.isArchived()
                && task.getReportedDays().isEmpty());
    }
}
