package ru.onotole.msuQuizApi.jpa;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.onotole.msuQuizApi.model.Task;

import javax.persistence.EntityManager;
import java.nio.file.FileSystems;
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

    @Value("${fileWithTasks}")
    private String fileWithTasks;

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
    public String reloadTasks() {
        return reloadTasksByStringPath(fileWithTasks);
    }

    @SneakyThrows
    public String reloadTasksByStringPath(String file) {
        clear();
        Path path = FileSystems.getDefault().getPath(file);
        return reloadTasksByPath(path);
    }

    @SneakyThrows
    public String reloadTasksByPath(Path path) {
        Files.lines(path).forEach(l -> addTaskFromLine(l, delimiter));
        return "Loaded " + getAll().size() + " tasks";
    }

    private void addTaskFromLine(String taskLine, String delimiter) {
        String desc = taskLine.split(delimiter)[0];
        Integer answer = Integer.valueOf(taskLine.split(delimiter)[1]);
        addTask(new Task(desc, answer));
    }

    public void addTask(Task task) {
        taskRepository.save(task);
    }

}
