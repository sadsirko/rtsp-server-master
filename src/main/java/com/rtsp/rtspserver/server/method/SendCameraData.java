package com.rtsp.rtspserver.server.method;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



public class SendCameraData {
    public void sendCameraData(String json) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("http://localhost:3000/api/cameras");
            post.setEntity(new StringEntity(json));
            post.setHeader("Content-type", "application/json");
            String response = EntityUtils.toString(client.execute(post).getEntity(), "UTF-8");
            System.out.println("Response from server: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}