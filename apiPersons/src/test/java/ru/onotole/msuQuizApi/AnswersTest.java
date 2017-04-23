package ru.onotole.msuQuizApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.onotole.msuQuizApi.controller.PersonController;
import ru.onotole.msuQuizApi.jpa.PersonRepository;
import ru.onotole.msuQuizApi.jpa.PersonService;
import ru.onotole.msuQuizApi.jpa.TaskService;
import ru.onotole.msuQuizApi.model.Person;

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
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TaskService taskService;

    @Before
    public void prepare() {
        taskService.setTaskApiUrl("http://localhost:8088/");
        personService.clear();
        personController.addUser(new Person().setId(uid));
        person = personController.getUser(uid);
        person.setTaskOrder("0,1,2,3");
        personRepository.save(person);
    }

    @Test
    public void checkAnswersTest() {
        personController.tryToGuess(uid, "start");
        person = personController.getUser(uid);
        personController.tryToGuess(uid, "commandName");
        person = personController.getUser(uid);
        assertEquals("1,2,3", person.getTaskOrder());
        assertEquals("" + person.getExpectedAnswer(), "" + answers[0]);
        personController.tryToGuess(uid, answers[0]);
        person = personController.getUser(uid);
        assertEquals("2,3", person.getTaskOrder());
        assertEquals("" + person.getExpectedAnswer(), "" + answers[1]);
        personController.tryToGuess(uid, answers[1]);
        person = personController.getUser(uid);
        personController.tryToGuess(uid, answers[2]);
        person = personController.getUser(uid);
        assertEquals("", person.getTaskOrder());
        assertEquals("" + person.getExpectedAnswer(), "" + answers[3]);
    }
}
