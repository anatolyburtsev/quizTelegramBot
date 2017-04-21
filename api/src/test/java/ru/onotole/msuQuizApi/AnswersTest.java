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
import ru.onotole.msuQuizApi.jpa.TaskService;
import ru.onotole.msuQuizApi.model.Person;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by onotole on 18/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AnswersTest {
    private Person person;
    private Long uid = 101L;
    private String[] answers = new String[]{"4","27","9","1024"};

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
        person.setTaskOrder("1,2,3,4");
        personRepository.save(person);
    }

    @Test
    public void checkAnswersTest() {
        personController.tryToGuess(uid, null);
        person = personController.getUser(uid);
        assertEquals("2,3,4", person.getTaskOrder());
        assertEquals("" + person.getExpectedAnswer(), "" + answers[0]);
        personController.tryToGuess(uid, answers[0]);
        person = personController.getUser(uid);
        assertEquals("3,4", person.getTaskOrder());
        assertEquals("" + person.getExpectedAnswer(), "" + answers[1]);
        personController.tryToGuess(uid, answers[1]);
        person = personController.getUser(uid);
        personController.tryToGuess(uid, answers[2]);
        person = personController.getUser(uid);
        assertEquals("", person.getTaskOrder());
        assertEquals("" + person.getExpectedAnswer(), "" + answers[3]);
    }
}
