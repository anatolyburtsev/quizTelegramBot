package ru.onotole.msuQuizApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.onotole.msuQuizApi.jpa.PersonService;
import ru.onotole.msuQuizApi.model.Person;
import ru.onotole.msuQuizApi.model.Task;

import java.util.List;

/**
 * Created by onotole on 16/04/2017.
 */
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public void addUser(@RequestBody Person person) {
        personService.add(person);
    }

    @RequestMapping("/user/{id}")
    public Person getUser(@PathVariable Long id) {
        return personService.getByUserId(id);
    }

    @RequestMapping(value = "/user/{id}/guess", method = RequestMethod.POST)
    public Task tryToGuess(@PathVariable Long id, @RequestParam String answer) {
        return personService.checkAndNextQuestion(id, answer);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public List<Person> getAll() {
        return personService.getAll();
    }

}
