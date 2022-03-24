package com.example.pwguide.timetable_download;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

public class ISODTimetableDownload {
    URIBuilder builder;

    public ISODTimetableDownload() {
        this.builder = new URIBuilder();
    }

    public TimetableISOD downloadTimetableFromISOD(String username, String apiKey, String semester) throws URISyntaxException, MalformedURLException {
        builder.setScheme("https");
        builder.setHost("isod.ee.pw.edu.pl");
        builder.setPath("/isod-portal/wapi");
        builder.addParameter("q", "myplan");
        builder.addParameter("username", username);
        builder.addParameter("semester", semester);
        builder.addParameter("apikey", apiKey);

        URI uri = builder.build().toURL().toURI();
        int timeout = 5;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000).build();

        TimetableISOD timeTable = null;

        try (CloseableHttpClient httpClient =  HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            HttpGet request = new HttpGet(uri);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if(response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String result = EntityUtils.toString(entity);
                        ObjectMapper objectMapper = new ObjectMapper();
                        timeTable = objectMapper.readValue(result, TimetableISOD.class);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return timeTable;
    }
}
