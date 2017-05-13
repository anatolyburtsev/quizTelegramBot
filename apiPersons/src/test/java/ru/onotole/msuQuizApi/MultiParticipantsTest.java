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
import ru.onotole.msuQuizApi.model.Response;
import ru.onotole.msuQuizApi.model.Task;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by onotole on 09/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiParticipantsTest {
    private Person person1;
    private Person person2;
    private final static Long UID1 = 112306L;
    private final static Long UID2 = 112309L;
    private String[] answers = {"40","41","42"};
    private String[] descriptions = { "desc0", "desc1", "desc2"};
    private final static String COMMAND_NAME1 = "SuperTeam123";
    private final static String COMMAND_NAME2 = "MegaTeam321";

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
        personController.addUser(new Person().setId(UID1));
        person1 = personController.getUser(UID1);
        personController.addUser(new Person().setId(UID2));
        person2 = personController.getUser(UID2);
        LinkedList<Integer> deque = new LinkedList<>();
        deque.addAll(Arrays.asList(0,1,2));
        person1.setTaskOrder(deque);
        person2.setTaskOrder(deque);
        personRepository.save(person1);
        personRepository.save(person2);
    }

    @Test
    public void test() {
        personController.tryToGuess(UID1, "start");
        personController.tryToGuess(UID2, "start");
        personController.tryToGuess(UID2, COMMAND_NAME2);
        personController.tryToGuess(UID1, COMMAND_NAME1);
        Response response = personController.tryToGuess(UID1, "stats");
        assertEquals("Название команды, Количество верно решенных, Количество взятых задач\n\n" +
                "0) SuperTeam123 : 0 : 1\n" +
                "1) MegaTeam321 : 0 : 1\n", response.getBody());
        personController.tryToGuess(UID2, answers[0]);
        personController.tryToGuess(UID1, "-10");
        personController.tryToGuess(UID1, "-10");
        personController.tryToGuess(UID1, "-10");
        response = personController.tryToGuess(UID1, "stats");

        assertEquals( "Название команды, Количество верно решенных, Количество взятых задач\n\n" +
                "0) MegaTeam321 : 1 : 2\n" +
                "1) SuperTeam123 : 0 : 2\n",
                response.getBody());
    }
}
