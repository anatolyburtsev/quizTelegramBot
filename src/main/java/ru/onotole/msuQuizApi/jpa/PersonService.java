package ru.onotole.msuQuizApi.jpa;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.onotole.msuQuizApi.model.Person;
import ru.onotole.msuQuizApi.model.Phrases;
import ru.onotole.msuQuizApi.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ru.onotole.msuQuizApi.model.Person.POST_LAST_TASK_ANSWER_FLAG;

/**
 * Created by onotole on 16/04/2017.
 */
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TaskService taskService;

    public Person getByUserId(Long uid) {
        return personRepository.findOne(uid);
    }

    public void add(Person person) {
        int taskCount = taskService.getAll().size();
        String taskOrder = getShuffledListString(taskCount);
        person.setTaskOrder(taskOrder);
        person.setBalls(0);
        personRepository.save(person);
    }

    public List<Person> getAll() {
        List<Person> list = new ArrayList<>();
        personRepository.findAll().forEach(list::add);
        return list;
    }


    public Task checkAndNextQuestion(Long id, String answerInputed) {
        Person person = getByUserId(id);
        Task resultTask;
        Integer answer;

        if (person == null) {
            add(new Person().setId(id));
            person = getByUserId(id);
        }

        // Start или что-то типа того пришло
        if (person.getCommandName().equals(Person.EMPTY_COMMAND_NAME_ANSWER_FLAG)) {
            person.setCommandName(Person.WAITING_COMMAND_NAME_ANSWER_FLAG);
            personRepository.save(person);
            resultTask = new Task(0, Phrases.WELCOME,0);
            return resultTask;
        } else

        // пришло название команды
        if (person.getCommandName().equals(Person.WAITING_COMMAND_NAME_ANSWER_FLAG)) {
            person.setCommandName(answerInputed);
            personRepository.save(person);
            resultTask = getNextTask(person);
            resultTask.addDescriptionPrefix(String.format(Phrases.LETS_START_GAME, Person.DEFAULT_ATTEMPTS));
            return resultTask;
        }

        try {
            answer = Integer.valueOf(answerInputed);
        } catch (NumberFormatException e) {
            // пришло не число
            // некорректный ввод, название команды задано и оно не дефолтное
            if (person.getCommandName() != null && ! person.getCommandName().equals(Person.WAITING_COMMAND_NAME_ANSWER_FLAG)) {
                return new Task(0, Phrases.INCORRECT_INPUT, 0);
            }
            throw new RuntimeException("что-то явно пошло не так!");
        }

        //finished game
        if (Objects.equals( person.getExpectedAnswer(), Person.POST_LAST_TASK_ANSWER_FLAG)) {
            resultTask = new Task(0, person.finish(), POST_LAST_TASK_ANSWER_FLAG);
        } else

        // right answer
        if (Objects.equals(person.getExpectedAnswer(), answer)) {
            //right answer
            person.addWinPoint();
            resultTask = getNextTask(person);
            // if not last right answer
            if (! Objects.equals(POST_LAST_TASK_ANSWER_FLAG, resultTask.getAnswer())) {
                resultTask.addDescriptionPrefix(Phrases.NEXT_TASK);
            }
            resultTask.addDescriptionPrefix(Phrases.RIGHT_ANSWER);
        } else

        //wrong answer
        if (person.makeAttempt()) {
           // wrong answer, not last attempt
           resultTask = new Task(0, String.format(Phrases.WRONG_ANSWER_LEFT_ATTEMPTS,
               person.getAttemptCounter()), POST_LAST_TASK_ANSWER_FLAG);
        } else {
           // wrong answer, no more attempts
           resultTask = getNextTask(person);
           resultTask.addDescriptionPrefix(Phrases.WRONG_ANSWER_NO_MORE_ATTEMPTS);
        }

        personRepository.save(person);
        return resultTask;
    }

    private Task getNextTask(Person person) {
        Task resultTask;
        Integer nextTaskNumber = person.getNextTaskNumber();
        if (nextTaskNumber < 0) {
            //right answer, no more question
            resultTask = new Task(0, person.finish(), POST_LAST_TASK_ANSWER_FLAG);
        } else {
            //right answer, not last question
            resultTask = taskService.getTask(nextTaskNumber);
        }
        person.setExpectedAnswer(resultTask.getAnswer());
        personRepository.save(person);
        return resultTask;
    }

    public void clear() {
        personRepository.deleteAll();
    }

    private String getShuffledListString(int size) {
        List<String> list = getShuffledList(size);
        return StringUtils.join(list, ",");
    }

    private List<String> getShuffledList(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < size + 1; i++) {
            list.add("" + i);
        }
        Collections.shuffle(list);
        return list;
    }
}
