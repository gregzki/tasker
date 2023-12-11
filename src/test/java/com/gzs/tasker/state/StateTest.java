package com.gzs.tasker.state;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StateTest {

    State state = new State();

    @BeforeEach
    void setUp() {
        Task task1 = new Task("1");
        task1.archive();
        task1.reportTime(0, 10);
        Task task2 = new Task("2");
        task2.archive();
        Task task3 = new Task("3");
        task3.reportTime(0, 5);
        state.getTasks().addAll(List.of(
                task1,
                task2,
                task3,
                new Task("4")));
    }

    @Test
    void addTask() {
        State testState = new State();

        testState.addTask("TestTask");

        assertThat(testState.tasks).hasSize(1);
        assertThat(testState.tasks.get(0).getTitle()).isEqualTo("TestTask");
    }

    @Test
    void getTasks() {
        List<Task> tasks = state.getTasks();
        Assertions.assertThat(tasks).hasSize(4);
        for (int i = 0; i < tasks.size(); i++) {
            assertThat(tasks.get(i).getTitle()).isEqualTo(String.valueOf(i + 1));
        }
    }

    @Test
    void getActiveTasks() {
        List<Task> activeTasks = state.getActiveTasks();
        Assertions.assertThat(activeTasks).hasSize(2);
        for (int i = 0; i < activeTasks.size(); i++) {
            assertThat(activeTasks.get(i).getTitle()).isEqualTo(String.valueOf(i + 3));
        }
    }

    @Test
    void cleanUp() {
        assertThat(state.tasks).hasSize(4);
        state.cleanUp();
        assertThat(state.tasks)
                .hasSize(3)
                .allMatch(t -> !(t.getReportedDays().isEmpty() && t.isArchived()));
    }
}