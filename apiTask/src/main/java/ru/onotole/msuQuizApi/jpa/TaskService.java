package ru.onotole.msuQuizApi.jpa;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.onotole.msuQuizApi.model.Task;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by onotole on 16/04/2017.
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    @Value("${delimiter}")
    private String delimiter;

    @Value("${fileWithTasks}")
    private String fileWithTasks;

    private final TaskRepository taskRepository;

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

    public String reloadTasks() {
        return reloadTasksByStringPath(fileWithTasks);
    }

    public String reloadTasksByStringPath(String file) {
        clear();
        Path path = Paths.get(file);
        return reloadTasksByPath(path);
    }

    @SneakyThrows(IOException.class)
    public String reloadTasksByPath(Path path) {
        Files.lines(path).forEach(this::addTaskFromLine);
        return "Loaded " + getAll().size() + " tasks";
    }

    private void addTaskFromLine(String taskLine) {
        String desc = taskLine.split(delimiter)[0];
        Integer answer = Integer.valueOf(taskLine.split(delimiter)[1]);
        addTask(new Task(desc, answer));
    }

    public void addTask(Task task) {
        taskRepository.save(task);
    }

}
