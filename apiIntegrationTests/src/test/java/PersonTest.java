import com.google.gson.Gson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

/**
 * Created by onotole on 23/04/2017.
 */
public class PersonTest {
    private static final Random random = new Random();
    private static final Long UID = Math.abs(random.nextLong());
    private static final String TASK_API_URL = "http://localhost:8088/";
    private static final String PERSON_API_URL = "http://localhost:8089/";
    private static final String GUESS_URL = "user/" + UID + "/guess";
    private Gson gson = new Gson();

    @BeforeClass
    public static void globalPrepare() {
        String url = TASK_API_URL + "task/reload";
        String response = SendRequests.sendPostWith1Param(url, "file", "tasks_test.txt");
        assumeThat(response, equalTo("Loaded 4 tasks"));
    }

    @Test
    public void checkMessages() throws InterruptedException {
        String url = PERSON_API_URL + GUESS_URL;
        long startTime = System.currentTimeMillis();
        String httpResponse = SendRequests.sendPostWith1Param(url, "answer", "start");
        Response response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().startsWith("Приветствую"));

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "command123");
        response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().startsWith("Начнем игру!"));


        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "command123");
        response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().startsWith("Я не понял что ты умеешь ввиду."));

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(),"Ответ неверный. Попыток осталось: 2 \n");

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(),"Ответ неверный. Попыток осталось: 1 \n");

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().startsWith("Ответ неверный. Больше попыток нет.\nСледующее задание:\n"));

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "command123");
        response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().startsWith("Я не понял что ты умеешь ввиду."));

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(),"Ответ неверный. Попыток осталось: 2 \n");

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(),"Ответ неверный. Попыток осталось: 1 \n");

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().startsWith("Ответ неверный. Больше попыток нет.\nСледующее задание:\n"));

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "command123");
        response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().startsWith("Я не понял что ты умеешь ввиду."));

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(),"Ответ неверный. Попыток осталось: 2 \n");

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(),"Ответ неверный. Попыток осталось: 1 \n");

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().startsWith("Ответ неверный. Больше попыток нет.\nСледующее задание:\n"));

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "command123");
        response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().startsWith("Я не понял что ты умеешь ввиду."));

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(),"Ответ неверный. Попыток осталось: 2 \n");

        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000 + 3;
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(),"Ответ неверный. Попыток осталось: 1 \n");

        Thread.sleep(3000);
        httpResponse = SendRequests.sendPostWith1Param(url, "answer", "-100");
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(), String.format("Ответ неверный. Больше попыток нет.\nПоздравляю! " +
                "Ваша команда command123 прошла игру за 00:00:0%d и набрала баллов: 0", elapsedTime));

    }
}
