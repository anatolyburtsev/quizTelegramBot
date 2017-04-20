package ru.onotole.msuQuizApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.onotole.msuQuizApi.controller.PersonController;
import ru.onotole.msuQuizApi.controller.TaskController;
import ru.onotole.msuQuizApi.jpa.PersonRepository;
import ru.onotole.msuQuizApi.jpa.PersonService;
import ru.onotole.msuQuizApi.model.Person;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by onotole on 18/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskDisOrderTest {
    private Person person;
    private Long uid = 103L;

    @Autowired
    private PersonController personController;

    @Autowired
    private TaskController taskController;

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
    }

    @Test
    public void checkAttemptCount() {
        assertNotEquals("1,2,3,4", person.getTaskOrder());
    }

}
