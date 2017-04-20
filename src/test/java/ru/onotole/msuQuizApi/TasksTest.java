package ru.onotole.msuQuizApi;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.onotole.msuQuizApi.controller.PersonController;
import ru.onotole.msuQuizApi.controller.TaskController;
import ru.onotole.msuQuizApi.jpa.PersonRepository;
import ru.onotole.msuQuizApi.jpa.PersonService;
import ru.onotole.msuQuizApi.jpa.TaskService;
import ru.onotole.msuQuizApi.model.Person;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by onotole on 16/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TasksTest {
    private Person person;
    private Long uid = 104L;

    @Autowired
    private PersonController personController;

    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void setupTask() {
        taskController.reloadTasksFromFile("tasks_test.txt");
    }

    @Before
    public void prepare() {
        personService.clear();
        personController.addUser(new Person().setId(uid));
        person = personController.getUser(uid);
        person.setTaskOrder("1,2,3,4");
        personRepository.save(person);
    }

    @Test
    public void checkExist() {
        assertEquals(personController.getAll().size(), 1);
    }

    @Test
    public void checkUserExistWithCorrectUID() {
        assertEquals(personController.getAll().get(0).getId(), uid);
    }

}
