package ru.onotole.msuQuizApi.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.onotole.msuQuizApi.model.Person;

/**
 * Created by onotole on 16/04/2017.
 */
public interface PersonRepository extends CrudRepository<Person, Long> {

}
