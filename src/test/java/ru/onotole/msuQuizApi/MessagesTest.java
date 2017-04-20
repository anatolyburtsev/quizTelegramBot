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
import ru.onotole.msuQuizApi.model.Phrases;
import ru.onotole.msuQuizApi.model.Task;

import static org.junit.Assert.assertEquals;

/**
 * Created by onotole on 19/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagesTest {
    private Person person;
    private Long uid = 106L;
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
    public void checkMessages() {
        Task task = personController.tryToGuess(uid, null);
        assertEquals( String.format(Phrases.WELCOME, Person.DEFAULT_ATTEMPTS) + "\n" +
                        taskController.getTaskById(1).getDescription(),
                task.getDescription());
        task = personController.tryToGuess(uid, answers[0]);
        assertEquals(Phrases.RIGHT_ANSWER + "\n" + Phrases.NEXT_TASK + "\n" + taskController.getTaskById(2).getDescription(),
                task.getDescription());
        task = personController.tryToGuess(uid, "-10");
        assertEquals(String.format(Phrases.WRONG_ANSWER_LEFT_ATTEMPTS, 2),
                task.getDescription());
        task = personController.tryToGuess(uid, "-10");
        assertEquals(String.format(Phrases.WRONG_ANSWER_LEFT_ATTEMPTS, 1),
                task.getDescription());
        task = personController.tryToGuess(uid, "-10");
        assertEquals(Phrases.WRONG_ANSWER_NO_MORE_ATTEMPTS + "\n" + taskController.getTaskById(3).getDescription(),
                task.getDescription());

        task = personController.tryToGuess(uid, answers[2]);

        person.setStart(
                person.getStart().minusHours(1).minusMinutes(2).minusSeconds(3)
        );
        task = personController.tryToGuess(uid, answers[3]);


        assertEquals(Phrases.RIGHT_ANSWER + "\n" + String.format(Phrases.CONGRATUATION, 1, 2, 3, 3),
                task.getDescription());

    }
}
