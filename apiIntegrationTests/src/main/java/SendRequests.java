import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onotole on 23/04/2017.
 */
public class SendRequests {

    @SneakyThrows(IOException.class)
    public static String sendGetReq(String url) {
//        log.info("make request to " + url);
        System.out.println("make request to " + url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        try (CloseableHttpResponse response = httpclient.execute(httpget)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String httpResponse = IOUtils.toString(instream, "UTF-8");
//                log.info("got from API: " + httpResponse);
                System.out.println("got from API: " + httpResponse);
                return httpResponse;
            }
        }
        throw new RuntimeException("API " + url + " unavailable");
    }

    @SneakyThrows(IOException.class)
    public static String sendPostReq(String url, Map<String, String> inputParams) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(inputParams.size());
        for (Map.Entry<String, String> entry : inputParams.entrySet())
        {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        //Execute and get the response.
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream instream = entity.getContent()) {
                String httpResponse = IOUtils.toString(instream, "UTF-8");
//                log.info("got from API: " + httpResponse);
                System.out.println("API response: " + httpResponse);
                return httpResponse;
            }
        }
        throw new RuntimeException("API " + url + " unavailable");
    }

    public static String sendPostWith1Param(String url, String key, String value) {
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        return sendPostReq(url, params);
    }
}
