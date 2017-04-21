package ru.onotole.msuQuizApi.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.onotole.msuQuizApi.model.Task;

/**
 * Created by onotole on 16/04/2017.
 */
public interface TaskRepository extends CrudRepository<Task, Integer> {
}
