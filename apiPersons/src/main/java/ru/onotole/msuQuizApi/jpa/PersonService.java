package ru.onotole.msuQuizApi.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.onotole.msuQuizApi.model.Person;
import ru.onotole.msuQuizApi.model.Phrases;
import ru.onotole.msuQuizApi.model.Response;
import ru.onotole.msuQuizApi.model.Task;

import java.util.*;

import static ru.onotole.msuQuizApi.model.Person.POST_LAST_TASK_ANSWER_FLAG;
import static ru.onotole.msuQuizApi.model.Phrases.NEXT_TASK;

/**
 * Created by onotole on 16/04/2017.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    private final TaskService taskService;

    public Person getByUserId(Long uid) {
        return personRepository.findOne(uid);
    }

    public void add(Person person) {
        int taskCount = taskService.getTasksAmount();
        LinkedList<Integer> taskOrder = getShuffledDeque(taskCount);
        person.setTaskOrder(taskOrder);
        person.setBalls(0);
        personRepository.save(person);
    }

    public List<Person> getAll() {
        List<Person> list = new ArrayList<>();
        personRepository.findAll().forEach(list::add);
        return list;
    }


    public Response checkAndNextQuestion(Long id, String answerInputed) {
        log.info("process req from uid: " + id + " msg: " + answerInputed);
        Person person = getByUserId(id);
        Response resultResponse;
        Integer answer;

        if (person == null) {
            add(new Person().setId(id));
            person = getByUserId(id);
        }

        // Start или что-то типа того пришло
        if (person.getCommandName().equals(Person.EMPTY_COMMAND_NAME_ANSWER_FLAG)) {
            person.setCommandName(Person.WAITING_COMMAND_NAME_ANSWER_FLAG);
            personRepository.save(person);
            resultResponse = new Response(Phrases.WELCOME);
            return resultResponse;
        } else

        // пришло название команды
        if (person.getCommandName().equals(Person.WAITING_COMMAND_NAME_ANSWER_FLAG)) {
            person.setCommandName(answerInputed);
            personRepository.save(person);
            Task nextTask = getNextTask(person);
            resultResponse = new Response(nextTask.getDescription());
            resultResponse.addPrefix(String.format(Phrases.LETS_START_GAME, Person.DEFAULT_ATTEMPTS));
            return resultResponse;
        }

        try {
            answer = Integer.valueOf(answerInputed);
        } catch (NumberFormatException e) {
            // пришло не число
            // некорректный ввод, название команды задано и оно не дефолтное
            if (person.getCommandName() != null && ! person.getCommandName().equals(Person.WAITING_COMMAND_NAME_ANSWER_FLAG)) {
                return new Response(Phrases.INCORRECT_INPUT);
            }
            throw new RuntimeException("что-то явно пошло не так!");
        }

        //finished game
        if (Objects.equals( person.getExpectedAnswer(), Person.POST_LAST_TASK_ANSWER_FLAG)) {
            resultResponse = new Response(person.finish());
        } else

        // right answer
        if (Objects.equals(person.getExpectedAnswer(), answer)) {
            //right answer
            person.addWinPoint();
            Task nextTask = getNextTask(person);
            resultResponse = new Response(nextTask.getDescription());
            // if not last right answer
            if (! Objects.equals(POST_LAST_TASK_ANSWER_FLAG, nextTask.getAnswer())) {
                resultResponse.addPrefix(NEXT_TASK);
            }
            resultResponse.addPrefix(Phrases.RIGHT_ANSWER);
        } else

        //wrong answer
        if (person.makeAttempt()) {
           // wrong answer, not last attempt
            resultResponse = new Response(String.format(Phrases.WRONG_ANSWER_LEFT_ATTEMPTS,
                    person.getAttemptCounter()));
        } else {
            // wrong answer, no more attempts
            Task nextTask = getNextTask(person);
            resultResponse = new Response(nextTask.getDescription());
            if (! Objects.equals(POST_LAST_TASK_ANSWER_FLAG, nextTask.getAnswer())) {
                resultResponse.addPrefix(NEXT_TASK);
            }
            resultResponse.addPrefix(Phrases.WRONG_ANSWER_NO_MORE_ATTEMPTS);
        }

        personRepository.save(person);
        return resultResponse;
    }

    private Task getNextTask(Person person) {
        Task resultTask;
        Integer nextTaskNumber = person.getNextTaskNumber();
        if (nextTaskNumber < 0) {
            //right answer, no more question
            resultTask = new Task(person.finish(), POST_LAST_TASK_ANSWER_FLAG);
        } else {
            //right answer, not last question
            resultTask = taskService.getTaskById(nextTaskNumber);
        }
        person.setExpectedAnswer(resultTask.getAnswer());
        personRepository.save(person);
        return resultTask;
    }

    public void clear() {
        personRepository.deleteAll();
    }

    private LinkedList<Integer> getShuffledDeque(int size) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }
}
