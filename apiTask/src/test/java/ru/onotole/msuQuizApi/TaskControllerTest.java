package ru.onotole.msuQuizApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.onotole.msuQuizApi.controller.TaskController;
import ru.onotole.msuQuizApi.model.Task;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by onotole on 22/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskControllerTest {
    private Task task = new Task("3 * 9 =", 27);

    @Autowired
    private TaskController taskController;

    @Before
    public void setup() {
        taskController.reloadTasks(null);
    }

    @Test
    public void taskCount() {
        assertThat(taskController.getSize(), equalTo(4));
        assertThat(taskController.getAll().size(), equalTo(4));
    }

    @Test
    public void taskNumber() {
        taskController.reloadTasks(null);
        assertEquals(taskController.getTaskById(1), task);
    }

}
