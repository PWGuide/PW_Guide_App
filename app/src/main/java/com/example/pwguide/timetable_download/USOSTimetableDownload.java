package com.example.pwguide.timetable_download;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.pwguide.R;
import com.example.pwguide.adapters.FragmentsTabAdapter;
import com.example.pwguide.model.Week;
import com.example.pwguide.model.WeekDay;
import com.example.pwguide.utils.AlertDialogsHelper;
import com.example.pwguide.utils.DbHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class USOSTimetableDownload {
    public static void USOSAuthorization(Activity activity, FragmentsTabAdapter adapter, AlertDialog usosAlert) {
        OAuthAuthenticator oAuthAuthenticator = new OAuthAuthenticator();

        int timeout = 5;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000).build();

        try (CloseableHttpClient httpClient =  HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            String uriRequestToken = oAuthAuthenticator.generateOAuthUriForUSOS("GET", UsosApiPaths.REQUEST_TOKEN_PATH, new String[][]{{"scopes", "studies"}, {"oauth_callback", "oob"}}, null);

            if (uriRequestToken != null) {
                HttpGet requestTokenRequest = new HttpGet(uriRequestToken);
                try (CloseableHttpResponse response = httpClient.execute(requestTokenRequest)) {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            String result = EntityUtils.toString(entity);

                            String requestToken = result.replace("oauth_token=", "");
                            int id = requestToken.indexOf("&");
                            requestToken = requestToken.substring(0, id);
                            int start = result.indexOf("=", result.indexOf("=") + 1);
                            int end = result.indexOf("&", start + 1);
                            String secretRequestToken = result.substring(start + 1, end);

                            URIBuilder builder = new URIBuilder();
                            builder.setScheme("https");
                            builder.setHost(UsosApiPaths.HOST_NAME);
                            builder.setPath(UsosApiPaths.AUTHORIZE_PATH);
                            builder.addParameter("oauth_token", requestToken);
                            URI auth_uri = null;
                            try {
                                auth_uri = builder.build().toURL().toURI();
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(auth_uri.toString()));
                            activity.startActivity(browserIntent);

                            final View addFromUSOSAlertLayout = activity.getLayoutInflater().inflate(R.layout.dialog_usos_pin, null);
                            usosAlert = AlertDialogsHelper.createEnterPINDialog(activity, addFromUSOSAlertLayout, adapter, requestToken, secretRequestToken);
                            usosAlert.show();
                        }
                    } else {
                        System.out.println(response.getStatusLine());
                        HttpEntity entity = response.getEntity();
                        System.out.println(EntityUtils.toString(entity));
                        Toast toast = Toast.makeText(activity, "Autoryzacja aplikacji nie powiodła się.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadTimetableFromUSOS(Activity activity, FragmentsTabAdapter adapter, AlertDialog dialog, String requestToken, String pin, String secretRequestToken) {
        OAuthAuthenticator oAuthAuthenticator = new OAuthAuthenticator();
        String accessTokenUri = oAuthAuthenticator.generateOAuthUriForUSOS("POST", UsosApiPaths.ACCESS_TOKEN_PATH, new String[][]{{"oauth_token", requestToken}, {"oauth_verifier", pin}}, secretRequestToken);
        if (accessTokenUri != null) {
            int timeout = 5;
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000).build();

            try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
                HttpPost requestAccessToken = new HttpPost(accessTokenUri);
                try (CloseableHttpResponse responseAccessToken = httpClient.execute(requestAccessToken)) {
                    if (responseAccessToken.getStatusLine().getStatusCode() == 200) {
                        dialog.dismiss();
                        HttpEntity entityAccessToken = responseAccessToken.getEntity();
                        if (entityAccessToken != null) {
                            String result = EntityUtils.toString(entityAccessToken);

                            String accessToken = result.replace("oauth_token=", "");
                            int id = accessToken.indexOf("&");
                            accessToken = accessToken.substring(0, id);
                            int start = result.indexOf("=", result.indexOf("=") + 1);
                            String secretAccessToken = result.substring(start + 1);

                            String uriDownloadTt = oAuthAuthenticator.generateOAuthUriForUSOS("POST", UsosApiPaths.STUDENT_TIMETABLE_PATH, new String[][]{{"oauth_token", accessToken}, {"fields", "start_time|end_time|course_name|classtype_name|building_name|building_id|room_number"}}, secretAccessToken);

                            if(uriDownloadTt != null) {
                                HttpPost requestTt = new HttpPost(uriDownloadTt);
                                try (CloseableHttpResponse responseTt = httpClient.execute(requestTt)) {
                                    if (responseTt.getStatusLine().getStatusCode() == 200) {
                                        HttpEntity entityTt = responseTt.getEntity();
                                        if (entityTt != null) {
                                            String resultTt = EntityUtils.toString(entityTt);
                                            ObjectMapper objectMapper = new ObjectMapper();
                                            TimetableUSOS[] timeTable = objectMapper.readValue(resultTt, TimetableUSOS[].class);
                                            insertTimetableToDatabase(timeTable, activity, adapter);

                                            Toast toast = Toast.makeText(activity, "Plan zajęć został pobrany.", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    } else {
                                        System.out.println(responseTt.getStatusLine());
                                        System.out.println(responseTt.toString());
                                        HttpEntity entityTt = responseTt.getEntity();
                                        String resultTt = EntityUtils.toString(entityTt);
                                        System.out.println(resultTt);
                                        Toast toast = Toast.makeText(activity, "Nie udało się pobrać planu zajęć.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println(responseAccessToken.getStatusLine());
                        Toast toast = Toast.makeText(activity, "Nie udało się przeprowadzić autoryzacji aplikacji. Upewnij się, że PIN jest poprawny.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void insertTimetableToDatabase(TimetableUSOS[] timetable, Activity activity, FragmentsTabAdapter adapter) {
        Random random = new Random();
        SimpleDateFormat format24 = new SimpleDateFormat("HH:mm");
        for (TimetableUSOS sub: timetable
        ) {
            final Week week = new Week();
            DbHelper dbHelper = new DbHelper(activity);
            int[] mColors = activity.getResources().getIntArray(R.array.default_colors);
            int no_color = random.nextInt(mColors.length);
            int selected_color = mColors[no_color];
            week.setSubject(sub.getCourseName());
            week.setType(sub.getClasstype());
            int day = sub.getStartTime().getDay() > 0 ? sub.getStartTime().getDay() : 7;
            String fragment = WeekDay.valueOf(day).getDayName();
            week.setFragment(fragment);
            week.setBuilding(sub.getBuilding());
            week.setRoom(sub.getRoom());
            week.setColor(selected_color);
            week.setFromTime(format24.format(sub.getStartTime()));
            week.setToTime(format24.format(sub.getEndTime()));
            dbHelper.insertWeek(week);

        }
        adapter.notifyDataSetChanged();
    }
}
