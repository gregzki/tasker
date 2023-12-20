package com.gzs.tasker.element;

import com.gzs.tasker.TasksDisplayHandler;
import com.gzs.tasker.state.Task;
import javafx.application.Platform;
//import org.awaitility.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.await;

class TaskButtonTest {

    @BeforeAll
    public static void setUpClass() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        Platform.startup(latch::countDown);

        latch.await();
    }

    TaskButton taskButton;

    @BeforeEach
    void setUp() {
        Task task = new Task("testTask");
        taskButton = new TaskButton(task, new TasksDisplayHandler());
    }

    @Test
    void play() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            assertThat(taskButton.timerValue).isZero();
            taskButton.play();

//            await().atLeast(Duration.ONE_SECOND);
            assertThat(taskButton.timerValue).isEqualTo(1);

            latch.countDown(); // Decrease the count of the latch
        });

        latch.await(); // Wait for the test code to complete
    }

    @Test
    void timerTickHandler() {
    }

    @Test
    void refresh() {
    }

    @Test
    void setTitle() {
    }

    @Test
    void resetCounter() {
    }

    @Test
    void removeThisButton() {
    }

    @Test
    void addMinutes() {
    }

    @Test
    void archiveTask() {
    }
}