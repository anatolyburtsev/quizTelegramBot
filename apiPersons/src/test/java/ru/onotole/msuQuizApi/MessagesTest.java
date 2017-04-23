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
import ru.onotole.msuQuizApi.model.Phrases;
import ru.onotole.msuQuizApi.model.Response;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static ru.onotole.msuQuizApi.model.Phrases.NEXT_TASK;

/**
 * Created by onotole on 19/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagesTest {
    private Person person;
    private final static Long UID = 106L;
    private String[] answers = new String[]{"4","27","9","1024"};
    private String[] questions = new String[] {
            "2 + 2 =",
            "3 * 9 =",
            "81 / 9 =",
            "2^10 ="
    };
    private final static String COMMAND_NAME = "SuperTeam123";

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
        personController.addUser(new Person().setId(UID));
        person = personController.getUser(UID);
        person.setTaskOrder("0,1,2,3");
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
                        questions[0],
                response.getBody());
        response = personController.tryToGuess(UID, answers[0]);
        assertEquals(Phrases.RIGHT_ANSWER + "\n" + NEXT_TASK + "\n" + questions[1],
                response.getBody());
        response = personController.tryToGuess(UID, "-10");
        assertEquals(String.format(Phrases.WRONG_ANSWER_LEFT_ATTEMPTS, 2),
                response.getBody());
        response = personController.tryToGuess(UID, "-10");
        assertEquals(String.format(Phrases.WRONG_ANSWER_LEFT_ATTEMPTS, 1),
                response.getBody());
        response = personController.tryToGuess(UID, "-10");
            assertEquals(Phrases.WRONG_ANSWER_NO_MORE_ATTEMPTS + "\n" + NEXT_TASK + "\n" + questions[2],
                response.getBody());

        response = personController.tryToGuess(UID, answers[2]);

        person = personController.getUser(UID);
        person.setStart(
                LocalDateTime.now().minusHours(1).minusMinutes(2).minusSeconds(3)
        );
        personRepository.save(person);
        response = personController.tryToGuess(UID, answers[3]);


        assertEquals(Phrases.RIGHT_ANSWER + "\n" + String.format(Phrases.CONGRATULATION, COMMAND_NAME, 1, 2, 3, 3),
                response.getBody());

    }
}
