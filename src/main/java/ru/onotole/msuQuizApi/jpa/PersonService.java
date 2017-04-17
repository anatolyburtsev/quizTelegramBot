package ru.onotole.msuQuizApi.jpa;

import com.sun.deploy.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.onotole.msuQuizApi.model.Person;
import ru.onotole.msuQuizApi.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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


    public Task checkAndNextQuestion(Long id, Integer answer) {
        Person person = getByUserId(id);
        if (Objects.equals(person.getExpectedAnswer(), answer)) {
            person.inc();
        }
        Integer nextTaskNumber = person.getNextTaskNumber();
        Task resultTask;
        if (nextTaskNumber < 0) {
            resultTask = new Task(0, person.finish(), 0);
        } else {
            resultTask = taskService.getTask(nextTaskNumber);
        }
        personRepository.save(person);
        return resultTask;
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
