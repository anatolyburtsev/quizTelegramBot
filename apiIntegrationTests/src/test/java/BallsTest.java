import com.google.gson.Gson;
import com.sun.org.apache.regexp.internal.RE;
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
public class BallsTest {
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
    public void checkBalls() throws InterruptedException {
        String answer = "1024";
        String url = PERSON_API_URL + GUESS_URL;
        long startTime = System.currentTimeMillis();
        SendRequests.sendPostWith1Param(url, "answer", "start");
        SendRequests.sendPostWith1Param(url, "answer", "command123");
        String httpResponse = "";
        for (int i = 0; i < 10; i++) {
            httpResponse = SendRequests.sendPostWith1Param(url, "answer", answer);
        }

        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        Response response = gson.fromJson(httpResponse, Response.class);
        assertTrue(response.getBody().endsWith(
                String.format("Поздравляю! " +
        "Ваша команда command123 прошла игру за 00:00:0%d и набрала баллов: 1", elapsedTime)));

        Thread.sleep(1000);
        httpResponse = SendRequests.sendPostWith1Param(url, "answer", answer);
        response = gson.fromJson(httpResponse, Response.class);
        assertEquals(response.getBody(),
                String.format("Поздравляю! " +
                        "Ваша команда command123 прошла игру за 00:00:0%d и набрала баллов: 1", elapsedTime));

    }
}
