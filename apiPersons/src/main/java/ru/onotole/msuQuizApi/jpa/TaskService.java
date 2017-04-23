package ru.onotole.msuQuizApi.jpa;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.onotole.msuQuizApi.model.Task;

import java.io.InputStream;

/**
 * Created by onotole on 22/04/2017.
 */
@Slf4j
@Component
public class TaskService {
    @Value("${task_api_host}")
    private String taskApiHost;

    @Value("${task_api_port}")
    private Integer taskApiPort;

    // TODO fix reading properties from file
    private String taskApiUrl = "http://localhost:8088/";//taskApiHost + ":" + taskApiPort + "/";
    private final static String TASK_URL = "task/";

    private final static String SIZE_URL = "task/size";

    @SneakyThrows
    public int getTasksAmount() {
        return Integer.valueOf(
                getResponseByUrl(taskApiUrl + SIZE_URL)
        );
    }

    public Task getTaskById(int id) {
        String responseJson = getResponseByUrl(
                taskApiUrl + TASK_URL + id
        );
        Gson gson = new Gson();
        return gson.fromJson(responseJson, Task.class);
    }

    public void setTaskApiUrl(String taskApiUrl) {
        this.taskApiUrl = taskApiUrl;
    }

    @SneakyThrows
    private String getResponseByUrl(String url) {
        log.info("request to " + url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        try (CloseableHttpResponse response = httpclient.execute(httpget)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String httpResponse = IOUtils.toString(instream, "UTF-8");
                log.info("API response: " + httpResponse);
                return httpResponse;
            }
        }
        throw new RuntimeException("API " + url + " unavailable");
    }
}
