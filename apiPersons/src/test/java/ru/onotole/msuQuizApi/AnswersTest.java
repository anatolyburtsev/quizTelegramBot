package ru.onotole.msuQuizApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.onotole.msuQuizApi.controller.PersonController;
import ru.onotole.msuQuizApi.jpa.PersonRepository;
import ru.onotole.msuQuizApi.jpa.PersonService;
import ru.onotole.msuQuizApi.jpa.TaskService;
import ru.onotole.msuQuizApi.model.Person;
import ru.onotole.msuQuizApi.model.Task;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by onotole on 18/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AnswersTest {
    private Person person;
    private Long uid = 101L;
    private String[] answers = {"123","234"};

    @Autowired
    private PersonController personController;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Mock
    private TaskService taskService;

    @Before
    public void mockTaskService() {
       when(taskService.getTaskById(1))
                .thenReturn(new Task("desc1", Integer.valueOf(answers[1])));
        when(taskService.getTaskById(0))
                .thenReturn(new Task("desc0", Integer.valueOf(answers[0])));
        when(taskService.getTasksAmount())
                .thenReturn(2);
        personService.setTaskService(taskService);
    }

    @Before
    public void prepare() {
        personService.clear();
        personController.addUser(new Person().setId(uid));
        person = personController.getUser(uid);
        LinkedList<Integer> deque = new LinkedList<>();
        deque.addAll(Arrays.asList(0,1));
        person.setTaskOrder(deque);
        personRepository.save(person);
    }

    @Test
    public void checkAnswersTest() {
        personController.tryToGuess(uid, "start");
        person = personController.getUser(uid);
        personController.tryToGuess(uid, "commandName");
        person = personController.getUser(uid);
        assertEquals("" + person.getExpectedAnswer(), "" + answers[0]);
        personController.tryToGuess(uid, answers[0]);
        person = personController.getUser(uid);
        assertEquals("" + person.getExpectedAnswer(), "" + answers[1]);
        personController.tryToGuess(uid, answers[1]);
        person = personController.getUser(uid);
        assertTrue(person.getTaskOrder().isEmpty());
    }
}
