import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

/**
 * Created by onotole on 23/04/2017.
 */
public class TaskTest {

    private static String TASK_API_URL = Config.TASK_API_URL;
    Gson gson = new Gson();

    @BeforeClass
    public static void globalPrepare() {
        String url = TASK_API_URL + "task/reload";
        String response = SendRequests.sendPostWith1Param(url, "file", "tasks_test.txt");
        assumeThat(response, equalTo("Loaded 4 tasks"));
    }

    @Test
    public void tasksCount() {
        String response = SendRequests.sendGetReq(TASK_API_URL + "task/size");
        assertEquals("4", response);
    }

    @Test
    public void taskWithNumber() {
        String response = SendRequests.sendGetReq(TASK_API_URL + "task/3");
        Task task = gson.fromJson(response, Task.class);
        assertThat(task.getAnswer(), equalTo(1024));
        assertThat(task.getDescription(), equalTo("2^10 ="));
    }

    @Test
    public void allTasks() {
        String response = SendRequests.sendGetReq(TASK_API_URL + "task");
        Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
        List<Task> list = new Gson().fromJson(response, listType);
        assertEquals(list.size(), 4);
        assertEquals(list.get(1).getDescription(), "3 * 9 =");
    }


}
