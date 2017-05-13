package ru.onotole.msuQuizApi.jpa;

import javafx.beans.binding.ObjectExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.onotole.msuQuizApi.model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.onotole.msuQuizApi.model.Person.*;
import static ru.onotole.msuQuizApi.model.Phrases.NEXT_TASK;
import static ru.onotole.msuQuizApi.model.Phrases.REST_TIME;

/**
 * Created by onotole on 16/04/2017.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    // I'd like to make it final, but I'll lost possibility to replace it with mock for testing
    @Autowired
    private TaskService taskService;

    public Person getByUserId(Long uid) {
        Person person = personRepository.findOne(uid);
        if (person == null) {
            add(new Person().setId(uid));
            person = personRepository.findOne(uid);
        }
        return person;
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

    public Response processRequest(Long id, String answer) {
        log.info("process req from uid: " + id + " msg: " + answer);
        Person person = getByUserId(id);
        LocalDateTime now = LocalDateTime.now();

        Response response = checkAndNextQuestion(person, answer);
        Duration delta = Duration.between(person.getStart(), now);

        if (! (Objects.equals(person.getCommandName(), EMPTY_COMMAND_NAME_ANSWER_FLAG) ||
                Objects.equals(person.getCommandName(), WAITING_COMMAND_NAME_ANSWER_FLAG) ||
                Objects.equals(person.getExpectedAnswer(), POST_LAST_TASK_ANSWER_FLAG) ||
                answer.equals("stats")
        )) {
            response.addPostfix(String.format(REST_TIME, Person.TIME_FOR_GAME - delta.toMinutes()));
        }
        return response;
    }

    private Response checkAndNextQuestion(Person person, String answerInputed) {
        Response resultResponse;
        LocalDateTime now = LocalDateTime.now();
        Integer answer;
        if ("stats".equals(answerInputed)) {
            return new Response(getParticipantStats());
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
            person.setStart(LocalDateTime.now());
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

        if (Duration.between(person.getStart(), now).toMinutes() > Person.TIME_FOR_GAME) {
            person.setExpectedAnswer(Person.POST_LAST_TASK_ANSWER_FLAG);
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

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public String getParticipantStats() {
        StringBuilder result = new StringBuilder("Название команды, Количество верно решенных, Количество взятых задач, Время в игре\n\n");
        LocalDateTime now = LocalDateTime.now();
        List<Person> persons = getAll().stream().sorted(Comparator.comparing(Person::getBalls).reversed())
                .collect(Collectors.toList());
        for (int i = 0; i < persons.size(); i++) {
            result.append(i);
            result.append(") ");
            result.append(persons.get(i).getCommandName());
            result.append(" : ");
            result.append(persons.get(i).getBalls());
            result.append(" : ");
            result.append(taskService.getTasksAmount() - persons.get(i).getTaskOrder().size());
            result.append(" : ");
            result.append(Duration.between(persons.get(i).getStart(), now).toMinutes());
            result.append(" мин");
            result.append("\n");
        }
        return result.toString();
    }
}
