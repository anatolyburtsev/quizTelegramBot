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
import ru.onotole.msuQuizApi.model.Phrases;
import ru.onotole.msuQuizApi.model.Response;
import ru.onotole.msuQuizApi.model.Task;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static ru.onotole.msuQuizApi.model.Phrases.NEXT_TASK;

/**
 * Created by onotole on 19/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagesTest {
    private Person person;
    private final static Long UID = 106L;
    private String[] answers = {"40","41","42"};
    private String[] descriptions = { "desc0", "desc1", "desc2"};
    private final static String COMMAND_NAME = "SuperTeam123";

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
        for (int i = 0; i < descriptions.length; i++) {
            when(taskService.getTaskById(i))
                    .thenReturn(new Task(descriptions[i], Integer.valueOf(answers[i])));
        }
        when(taskService.getTasksAmount())
                .thenReturn(descriptions.length);
        personService.setTaskService(taskService);
    }

    @Before
    public void prepare() {
        personService.clear();
        personController.addUser(new Person().setId(UID));
        person = personController.getUser(UID);
        LinkedList<Integer> deque = new LinkedList<>();
        deque.addAll(Arrays.asList(0,1,2));
        person.setTaskOrder(deque);
        personRepository.save(person);
    }

    @Test
    public void checkMessages() {
        Response response = personController.tryToGuess(UID, "start");
        assertEquals(Phrases.WELCOME, response.getBody());

        response = personController.tryToGuess(UID, COMMAND_NAME);
        person = personController.getUser(UID);
        assertEquals(person.getCommandName(), COMMAND_NAME);
        assertEquals( String.format(Phrases.LETS_START_GAME, Person.DEFAULT_ATTEMPTS) + "\n" +
                        descriptions[0],
                response.getBody());
        response = personController.tryToGuess(UID, answers[0]);
        assertEquals(Phrases.RIGHT_ANSWER + "\n" + NEXT_TASK + "\n" + descriptions[1],
                response.getBody());
        response = personController.tryToGuess(UID, "-10");
        assertEquals(String.format(Phrases.WRONG_ANSWER_LEFT_ATTEMPTS, 2),
                response.getBody());
        response = personController.tryToGuess(UID, "-10");
        assertEquals(String.format(Phrases.WRONG_ANSWER_LEFT_ATTEMPTS, 1),
                response.getBody());
        response = personController.tryToGuess(UID, "-10");
            assertEquals(Phrases.WRONG_ANSWER_NO_MORE_ATTEMPTS + "\n" + NEXT_TASK + "\n" + descriptions[2],
                response.getBody());

        person = personController.getUser(UID);
        person.setStart(
                LocalDateTime.now().minusHours(1).minusMinutes(2).minusSeconds(3)
        );
        personRepository.save(person);
        response = personController.tryToGuess(UID, answers[2]);


        assertEquals(Phrases.RIGHT_ANSWER + "\n" + String.format(Phrases.CONGRATULATION, COMMAND_NAME, 1, 2, 3, 2),
                response.getBody());

    }
}
