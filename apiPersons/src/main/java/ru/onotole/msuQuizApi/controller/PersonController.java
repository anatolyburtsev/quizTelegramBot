package ru.onotole.msuQuizApi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.onotole.msuQuizApi.jpa.PersonService;
import ru.onotole.msuQuizApi.model.Person;
import ru.onotole.msuQuizApi.model.Response;

import java.util.List;

/**
 * Created by onotole on 16/04/2017.
 */
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public void addUser(@RequestBody Person person) {
        personService.add(person);
    }

    @RequestMapping("/user/{id}")
    public Person getUser(@PathVariable Long id) {
        return personService.getByUserId(id);
    }

    @RequestMapping(value = "/user/{id}/guess", method = RequestMethod.POST)
    public Response tryToGuess(@PathVariable Long id, @RequestParam String answer) {
        return personService.processRequest(id, answer);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public List<Person> getAll() {
        return personService.getAll();
    }
}
