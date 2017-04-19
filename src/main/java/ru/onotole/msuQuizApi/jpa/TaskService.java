package ru.onotole.msuQuizApi.jpa;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.onotole.msuQuizApi.model.Task;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by onotole on 16/04/2017.
 */
@Service
public class TaskService {

    @Value("${delimiter}")
    private String delimiter;

    @Autowired
    private TaskRepository taskRepository;

    public Task getTask(Integer id) {
        return taskRepository.findOne(id);
    }

    public List<Task> getAll() {
        List<Task> resultList = new ArrayList<>();
        for(Task task: taskRepository.findAll()) {
            resultList.add(task);
        }
        return resultList;
    }

    public void delete(Integer id) {
        taskRepository.delete(id);
    }

    public void clear() {
        taskRepository.deleteAll();
    }

    @SneakyThrows
    public void reloadTasks(Path file) {
        Files.lines(file).forEach(l -> addTaskFromLine(l, delimiter));
    }

    private void addTaskFromLine(String taskLine, String delimiter) {
        Task task = new Task();
        task.setDescription(taskLine.split(delimiter)[0]);
        Integer answer = Integer.valueOf(taskLine.split(delimiter)[1]);
        task.setAnswer(answer);
        addTask(task);
    }

    public void addTask(Task task) {
        taskRepository.save(task);
    }

}
