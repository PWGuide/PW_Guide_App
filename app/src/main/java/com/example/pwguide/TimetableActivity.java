package com.example.pwguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.pwguide.adapters.FragmentsTabAdapter;
import com.example.pwguide.fragments.FridayFragment;
import com.example.pwguide.fragments.MondayFragment;
import com.example.pwguide.fragments.SaturdayFragment;
import com.example.pwguide.fragments.SundayFragment;
import com.example.pwguide.fragments.ThursdayFragment;
import com.example.pwguide.fragments.TuesdayFragment;
import com.example.pwguide.fragments.WednesdayFragment;
import com.example.pwguide.model.WeekDay;
import com.example.pwguide.timetable_download.OAuthAuthenticator;
import com.example.pwguide.timetable_download.USOSTimetableDownload;
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
import java.util.HashSet;
import java.util.Set;

public class TimetableActivity extends AppCompatActivity {


    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    private AlertDialog isodAlert;
    private AlertDialog usosAlert;
    private AlertDialog clearTimetableAlert;
    private AlertDialog chooseDaysToDisplayAlert;
    private final String STORAGE_NAME = "timetablePreferences";
    public static final String DISPLAYED_DAYS_PREFERENCE = "displayed_days";
    private SharedPreferences sharedPreferences;
    private Set<String> displayedDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_timetable);
        getSupportActionBar().setTitle("Plan zajęć");
        //getSupportActionBar().hide();
        initDaysSet();
        sharedPreferences = getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        if(!sharedPreferences.contains(DISPLAYED_DAYS_PREFERENCE)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(DISPLAYED_DAYS_PREFERENCE, displayedDays);
            editor.apply();
        } else {
            displayedDays = sharedPreferences.getStringSet(DISPLAYED_DAYS_PREFERENCE, displayedDays);
        }

        initAll();
    }

    private void initDaysSet() {
        displayedDays = new HashSet<>();
        displayedDays.add(WeekDay.MONDAY.getDayNamePL());
        displayedDays.add(WeekDay.TUESDAY.getDayNamePL());
        displayedDays.add(WeekDay.WEDNESDAY.getDayNamePL());
        displayedDays.add(WeekDay.THURSDAY.getDayNamePL());
        displayedDays.add(WeekDay.FRIDAY.getDayNamePL());
        displayedDays.add(WeekDay.SATURDAY.getDayNamePL());
        displayedDays.add(WeekDay.SUNDAY.getDayNamePL());
    }

    private void initAll() {
        setupFragments();
        setupCustomDialog();

        final View addFromISODAlertLayout = getLayoutInflater().inflate(R.layout.download_from_isod_dialog, null);
        isodAlert = AlertDialogsHelper.createAddFromISODDialog(this, addFromISODAlertLayout, adapter);

        final View clearTimetableAlertLayout = getLayoutInflater().inflate(R.layout.dialog_clear_timetable, null);
        clearTimetableAlert = AlertDialogsHelper.createClearTimetableDialog(this, clearTimetableAlertLayout, adapter);

        final View chooseDaysToDisplayAlertLayout = getLayoutInflater().inflate(R.layout.dialog_choose_days_to_display, null);
        chooseDaysToDisplayAlert = AlertDialogsHelper.createSelectDaysToDisplayDialog(this, chooseDaysToDisplayAlertLayout, adapter, sharedPreferences);
    }

    private void setupFragments() {
        adapter = new FragmentsTabAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        for (String dayName : displayedDays) {
            addFragment(dayName);
        }
        viewPager.setAdapter(adapter);
        int position = adapter.getPosition(day);
        viewPager.setCurrentItem(Math.max(position, 0), true);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addFragment(String dayName) {
        switch (dayName) {
            case "Poniedziałek":
                adapter.addFragment(new MondayFragment(), WeekDay.MONDAY.getDayNamePL());
                break;
            case "Wtorek":
                adapter.addFragment(new TuesdayFragment(), WeekDay.TUESDAY.getDayNamePL());
                break;
            case "Środa":
                adapter.addFragment(new WednesdayFragment(), WeekDay.WEDNESDAY.getDayNamePL());
                break;
            case "Czwartek":
                adapter.addFragment(new ThursdayFragment(), WeekDay.THURSDAY.getDayNamePL());
                break;
            case "Piątek":
                adapter.addFragment(new FridayFragment(), WeekDay.FRIDAY.getDayNamePL());
                break;
            case "Sobota":
                adapter.addFragment(new SaturdayFragment(), WeekDay.SATURDAY.getDayNamePL());
                break;
            case "Niedziela":
                adapter.addFragment(new SundayFragment(), WeekDay.SUNDAY.getDayNamePL());
                break;
            default:
                break;
        }
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
                USOSTimetableDownload.USOSAuthorization(this, adapter, usosAlert);
                return true;
            case R.id.edit_days:
                chooseDaysToDisplayAlert.show();
                return true;
            case R.id.clear_timetable:
                clearTimetableAlert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}