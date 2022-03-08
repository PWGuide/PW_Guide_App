package com.example.pwguide;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.pwguide.adapters.FragmentsTabAdapter;
import com.example.pwguide.fragments.FridayFragment;
import com.example.pwguide.fragments.MondayFragment;
import com.example.pwguide.fragments.SaturdayFragment;
import com.example.pwguide.fragments.SundayFragment;
import com.example.pwguide.fragments.ThursdayFragment;
import com.example.pwguide.fragments.TuesdayFragment;
import com.example.pwguide.fragments.WednesdayFragment;
import com.example.pwguide.timetable_download.OAuthAuthenticator;
import com.example.pwguide.timetable_download.UsosApiPaths;
import com.example.pwguide.utils.AlertDialogsHelper;
import com.google.android.material.tabs.TabLayout;

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

public class TimetableActivity extends AppCompatActivity {


    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    private AlertDialog isodAlert;
    private AlertDialog usosAlert;
    private AlertDialog clearTimetableAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_timetable);
        getSupportActionBar().setTitle("Plan zajęć");
        //getSupportActionBar().hide();

        initAll();
    }

    private void initAll() {
        setupFragments();
        setupCustomDialog();

        final View addFromISODAlertLayout = getLayoutInflater().inflate(R.layout.download_from_isod_dialog, null);
        isodAlert = AlertDialogsHelper.createAddFromISODDialog(this, addFromISODAlertLayout, adapter);

        final View clearTimetableAlertLayout = getLayoutInflater().inflate(R.layout.dialog_clear_timetable, null);
        clearTimetableAlert = AlertDialogsHelper.createClearTimetableDialog(this, clearTimetableAlertLayout, adapter);
    }

    private void setupFragments() {
        adapter = new FragmentsTabAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        adapter.addFragment(new MondayFragment(), getResources().getString(R.string.monday));
        adapter.addFragment(new TuesdayFragment(), getResources().getString(R.string.tuesday));
        adapter.addFragment(new WednesdayFragment(), getResources().getString(R.string.wednesday));
        adapter.addFragment(new ThursdayFragment(), getResources().getString(R.string.thursday));
        adapter.addFragment(new FridayFragment(), getResources().getString(R.string.friday));
        adapter.addFragment(new SaturdayFragment(), getResources().getString(R.string.saturday));
        adapter.addFragment(new SundayFragment(), getResources().getString(R.string.sunday));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(day == 1 ? 6 : day - 2, true);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupCustomDialog() {
        final View alertLayout = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        AlertDialogsHelper.getAddSubjectDialog(TimetableActivity.this, alertLayout, adapter, viewPager);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timetable_menu, menu);
        MenuItem item = menu.getItem(3);
        SpannableString spannableString = new SpannableString(item.getTitle().toString());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#CE0000")), 0, spannableString.length(), 0);
        item.setTitle(spannableString);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_from_isod:
                isodAlert.show();
                return true;
            case R.id.add_from_usos:
                USOSAuthorization();
                return true;
            case R.id.edit_days:
                //przejść do ustawień jakie dni są widoczne
                return true;
            case R.id.clear_timetable:
                clearTimetableAlert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void USOSAuthorization() {
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
                            startActivity(browserIntent);

                            final View addFromUSOSAlertLayout = getLayoutInflater().inflate(R.layout.dialog_usos_pin, null);
                            usosAlert = AlertDialogsHelper.createEnterPINDialog(this, addFromUSOSAlertLayout, adapter, requestToken, secretRequestToken);
                            usosAlert.show();
                        }
                    } else {
                        System.out.println(response.getStatusLine());
                        HttpEntity entity = response.getEntity();
                        System.out.println(EntityUtils.toString(entity));
                        Toast toast = Toast.makeText(this, "Autoryzacja aplikacji nie powiodła się.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}