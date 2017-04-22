package ru.onotole.msuQuizApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.onotole.msuQuizApi.jpa.TaskService;
import ru.onotole.msuQuizApi.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by onotole on 16/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TasksServiceTest {
    private Task task;

    @Autowired
    private TaskService taskService;

    @Before
    public void setup() {
        taskService.clear();
        task = new Task( "test", 10);
    }

    @After
    public void tearDown() {
        taskService.clear();
    }

    @Test
    public void emptyTaskService() {
        assertThat(taskService.getAll(), is(empty()));
    }

    @Test
    public void addTask() {
        taskService.addTask(task);
        assertEquals(taskService.getAll().size(), 1);
        Integer taskId = taskService.getAll().get(0).getId();
        assertEquals(taskService.getTask(taskId), task);
        System.out.println(task);
    }

    @Test
    public void removeTask() {
        taskService.addTask(task);
        Integer taskId = taskService.getAll().get(0).getId();
        taskService.delete(taskId);
        assertThat(taskService.getAll(), is(empty()));
        assertNull(taskService.getTask(taskId));
    }

    @Test
    public void reloadTasks() {
        taskService.reloadTasks();
        assertEquals(taskService.getAll().size(), 4);
        Task taskFromEx = new Task("2 + 2 =", 4);
        Integer taskId = taskService.getAll().get(0).getId();
        assertEquals(taskService.getTask(taskId), taskFromEx);
    }
}
