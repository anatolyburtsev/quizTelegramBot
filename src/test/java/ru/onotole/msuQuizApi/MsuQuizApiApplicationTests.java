package ru.onotole.msuQuizApi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.onotole.msuQuizApi.controller.PersonController;
import ru.onotole.msuQuizApi.controller.TaskController;
import ru.onotole.msuQuizApi.jpa.PersonService;
import ru.onotole.msuQuizApi.jpa.TaskService;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MsuQuizApiApplicationTests {

	@Autowired
	private PersonController personController;

	@Autowired
	private TaskController taskController;

	@Autowired
	private TaskService taskService;

	@Test
	public void contextLoads() {
		assertEquals(taskController.getAll().size(), 0);
		taskService.clear();
		Path path = FileSystems.getDefault().getPath("tasks_test.txt");
		taskService.reloadTasks(path);
		assertEquals(taskController.getAll().size(), 4);
	}

}
