package ru.onotole.msuQuizApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.onotole.msuQuizApi.model.Task;
import ru.onotole.msuQuizApi.jpa.TaskService;

import java.util.List;

/**
 * Created by onotole on 16/04/2017.
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public Task getTaskById(@PathVariable Integer id) {
        return taskService.getAll().get(id);
    }
//
//    @RequestMapping(value = "/task/{id}", method = RequestMethod.DELETE)
//    public void deleteTaskById(@PathVariable Integer id) {
//        taskService.delete(id);
//    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public List<Task> getAll() {
        return taskService.getAll();
    }

    @RequestMapping(value = "/task/size", method = RequestMethod.GET)
    public Integer getSize() {
        return taskService.getAll().size();
    }

//    @RequestMapping(value = "/task", method = RequestMethod.DELETE)
//    public void clearAllTasks() {
//        taskService.clear();
//    }
//
//    @RequestMapping(value = "/task", method = RequestMethod.POST)
//    public void addTask(@RequestBody Task task) {
//        taskService.addTask(task);
//    }

    @RequestMapping(value = "/task/reload", method = RequestMethod.POST)
    public String reloadTasks(@RequestParam(value = "file", required = false) String file) {
        if (file != null) {
            return taskService.reloadTasksByStringPath(file);
        } else {
            return taskService.reloadTasks();
        }
    }

}
