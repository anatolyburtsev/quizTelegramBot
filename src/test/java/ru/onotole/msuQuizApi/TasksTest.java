package ru.onotole.msuQuizApi;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.onotole.msuQuizApi.controller.PersonController;
import ru.onotole.msuQuizApi.controller.TaskController;
import ru.onotole.msuQuizApi.jpa.TaskService;
import ru.onotole.msuQuizApi.model.Person;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * Created by onotole on 16/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TasksTest {
    private Person person;
    private Long uid = 100L;
    private Integer[] answers = new Integer[]{4,27,9,1024};

    @Autowired
    private PersonController personController;

    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskService taskService;

    @Before
    public void prepare() {
        taskService.clear();
        Path path = FileSystems.getDefault().getPath("tasks_test.txt");
        taskService.reloadTasks(path);
        personController.addUser(new Person().setId(uid));
        person = personController.getUser(uid);
        person.setTaskOrder("1,2,3,4");
    }

    @Test
    public void checkExist() {
        assertEquals(personController.getAll().size(), 1);
    }

    @Test
    public void checkUserExistWithCorrectUID() {
        assertEquals(personController.getAll().get(0).getId(), uid);
    }

    @Test
    public void checkBalls() {
        assertEquals("" + person.getBalls(), "0");
        personController.startGuess(uid);
        assertEquals("2,3,4", person.getExpectedAnswer());
        assertEquals("" + person.getExpectedAnswer(), "" + answers[0]);
    }

}
