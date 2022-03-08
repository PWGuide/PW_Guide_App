package com.example.pwguide.utils;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.pwguide.R;
import com.example.pwguide.adapters.FragmentsTabAdapter;
import com.example.pwguide.adapters.WeekAdapter;
import com.example.pwguide.model.Week;
import com.example.pwguide.model.WeekDay;
import com.example.pwguide.timetable_download.OAuthAuthenticator;
import com.example.pwguide.timetable_download.TimetableDownload;
import com.example.pwguide.timetable_download.TimetableISOD;
import com.example.pwguide.timetable_download.UsosApiPaths;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ulan on 22.10.2018.
 */
public class AlertDialogsHelper {

    public static void getEditSubjectDialog(final Activity activity, final View alertLayout, final ArrayList<Week> adapter, final ListView listView, int position) {
        final HashMap<Integer, EditText> editTextHashs = new HashMap<>();
        final EditText subject = alertLayout.findViewById(R.id.subject_dialog);
        editTextHashs.put(R.string.subject, subject);
        final EditText class_type = alertLayout.findViewById(R.id.class_type_dialog);
        editTextHashs.put(R.string.class_type, class_type);
        final EditText building = alertLayout.findViewById(R.id.building_dialog);
        editTextHashs.put(R.string.building, building);
        final EditText room = alertLayout.findViewById(R.id.room_dialog);
        editTextHashs.put(R.string.room, room);
        final TextView from_time = alertLayout.findViewById(R.id.from_time);
        final TextView to_time = alertLayout.findViewById(R.id.to_time);
        final Button select_color = alertLayout.findViewById(R.id.select_color);
        final Week week = adapter.get(position);

        subject.setText(week.getSubject());
        class_type.setText(week.getType());
        building.setText(week.getBuilding());
        room.setText(week.getRoom());
        from_time.setText(week.getFromTime());
        to_time.setText(week.getToTime());
        select_color.setBackgroundColor(week.getColor() != 0 ? week.getColor() : Color.WHITE);

        from_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                from_time.setText(String.format("%02d:%02d", hourOfDay, minute));
                                week.setFromTime(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.setTitle(R.string.choose_time);
                timePickerDialog.show();
            }
        });

        to_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                to_time.setText(String.format("%02d:%02d", hourOfDay, minute));
                                week.setToTime(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.setTitle(R.string.choose_time);
                timePickerDialog.show();
            }
        });

        select_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mSelectedColor = ContextCompat.getColor(activity, R.color.white);
                select_color.setBackgroundColor(mSelectedColor);
                int[] mColors = activity.getResources().getIntArray(R.array.default_colors);
                ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                        mColors,
                        mSelectedColor,
                        5,
                        ColorPickerDialog.SIZE_SMALL,
                        true
                );

                dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        select_color.setBackgroundColor(color);
                        select_color.setText("color selected");
                    }
                });
                dialog.show(activity.getFragmentManager(), "color_dialog");
            }
        });

        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
      //  alert.setTitle(R.string.edit_subject);
        alert.setCancelable(false);
        final Button cancel = alertLayout.findViewById(R.id.cancel);
        final Button save = alertLayout.findViewById(R.id.save);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(subject.getText()) || TextUtils.isEmpty(room.getText())) {
                    for (Map.Entry<Integer, EditText> entry : editTextHashs.entrySet()) {
                        if(TextUtils.isEmpty(entry.getValue().getText())) {
                            entry.getValue().setError(activity.getResources().getString(entry.getKey()) + " " + activity.getResources().getString(R.string.field_error));
                            entry.getValue().requestFocus();
                        }
                    }
                } else if(!from_time.getText().toString().matches(".*\\d+.*") || !to_time.getText().toString().matches(".*\\d+.*")) {
                    Snackbar.make(alertLayout, R.string.time_error, Snackbar.LENGTH_LONG).show();
                } else {
                    DbHelper db = new DbHelper(activity);
                    WeekAdapter weekAdapter = (WeekAdapter) listView.getAdapter(); // In order to get notifyDataSetChanged() method.
                    ColorDrawable buttonColor = (ColorDrawable) select_color.getBackground();
                    week.setSubject(subject.getText().toString());
                    week.setType(class_type.getText().toString());
                    week.setBuilding(building.getText().toString());
                    week.setRoom(room.getText().toString());
                    week.setColor(buttonColor.getColor());
                    db.updateWeek(week);
                    weekAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }

    public static void getAddSubjectDialog(final Activity activity, final View alertLayout, final FragmentsTabAdapter adapter, final ViewPager viewPager) {
        final HashMap<Integer, EditText> editTextHashs = new HashMap<>();
        final EditText subject = alertLayout.findViewById(R.id.subject_dialog);
        editTextHashs.put(R.string.subject, subject);
        final EditText class_type = alertLayout.findViewById(R.id.class_type_dialog);
        editTextHashs.put(R.string.class_type, class_type);
        final EditText building = alertLayout.findViewById(R.id.building_dialog);
        editTextHashs.put(R.string.building, building);
        final EditText room = alertLayout.findViewById(R.id.room_dialog);
        editTextHashs.put(R.string.room, room);
        final TextView from_time = alertLayout.findViewById(R.id.from_time);
        final TextView to_time = alertLayout.findViewById(R.id.to_time);
        final Button select_color = alertLayout.findViewById(R.id.select_color);
        final Week week = new Week();

        from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                from_time.setText(String.format("%02d:%02d", hourOfDay, minute));
                                week.setFromTime(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.setTitle(R.string.choose_time);
                timePickerDialog.show(); }});

        to_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                to_time.setText(String.format("%02d:%02d", hourOfDay, minute));
                                week.setToTime(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.setTitle(R.string.choose_time);
                timePickerDialog.show();
            }
        });

        select_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mSelectedColor = ContextCompat.getColor(activity, R.color.white);
                select_color.setBackgroundColor(mSelectedColor);
                int[] mColors = activity.getResources().getIntArray(R.array.default_colors);
                ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                        mColors,
                        mSelectedColor,
                        5,
                        ColorPickerDialog.SIZE_SMALL,
                        true
                );

                dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        select_color.setBackgroundColor(color);
                        select_color.setText("color selected");
                    }
                });
                dialog.show(activity.getFragmentManager(), "color_dialog");
            }
        });

        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        // alert.setTitle(R.string.add_subject);
        alert.setCancelable(false);
        Button cancel = alertLayout.findViewById(R.id.cancel);
        Button submit = alertLayout.findViewById(R.id.save);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        FloatingActionButton fab = activity.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(subject.getText()) || TextUtils.isEmpty(room.getText())) {
                    for (Map.Entry<Integer, EditText> entry : editTextHashs.entrySet()) {
                        if(TextUtils.isEmpty(entry.getValue().getText())) {
                            entry.getValue().setError(activity.getResources().getString(entry.getKey()) + " " + activity.getResources().getString(R.string.field_error));
                            entry.getValue().requestFocus();
                        }
                    }
                } else if(!from_time.getText().toString().matches(".*\\d+.*") || !to_time.getText().toString().matches(".*\\d+.*")) {
                    Snackbar.make(alertLayout, R.string.time_error, Snackbar.LENGTH_LONG).show();
                } else {
                    DbHelper dbHelper = new DbHelper(activity);
                    Matcher fragment = Pattern.compile("(.*Fragment)").matcher(adapter.getItem(viewPager.getCurrentItem()).toString());
                    ColorDrawable buttonColor = (ColorDrawable) select_color.getBackground();
                    week.setSubject(subject.getText().toString());
                    week.setType(class_type.getText().toString());
                    week.setFragment(fragment.find() ? fragment.group().replace("Fragment", "") : null);
                    week.setBuilding(building.getText().toString());
                    week.setRoom(room.getText().toString());
                    week.setColor(buttonColor.getColor());
                    dbHelper.insertWeek(week);
                    adapter.notifyDataSetChanged();
                    subject.getText().clear();
                    class_type.getText().clear();
                    building.getText().clear();
                    room.getText().clear();
                    from_time.setText(R.string.select_time);
                    to_time.setText(R.string.select_time);
                    select_color.setBackgroundColor(Color.WHITE);
                    subject.requestFocus();
                    dialog.dismiss();
                }
            }
        });
    }

    public static AlertDialog createAddFromISODDialog(Activity activity, View alertLayout, FragmentsTabAdapter adapter){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();

        Button cancel = alertLayout.findViewById(R.id.timetable_cancel);
        Button download = alertLayout.findViewById(R.id.timetable_download);
        EditText usernameView = alertLayout.findViewById(R.id.timetable_username);
        EditText apiKeyView = alertLayout.findViewById(R.id.timetable_apiKey);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameView.getText().clear();
                apiKeyView.getText().clear();
                usernameView.requestFocus();
                dialog.dismiss();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameView.getText().toString();
                String apiKey = apiKeyView.getText().toString();

                TimetableDownload timetableDownload = new TimetableDownload();
                try {
                    TimetableISOD timetableISOD = timetableDownload.downloadTimetableFromISOD(username, apiKey);
                    if(timetableISOD != null) {
                        Random random = new Random();
                        SimpleDateFormat format24 = new SimpleDateFormat("HH:mm");
                        for (TimetableISOD.Subject sub: timetableISOD.getPlanItems()
                        ) {
                            final Week week = new Week();
                            DbHelper dbHelper = new DbHelper(activity);
                            int[] mColors = activity.getResources().getIntArray(R.array.default_colors);
                            int no_color = random.nextInt(mColors.length);
                            int selected_color = mColors[no_color];
                            week.setSubject(sub.getCourseName());
                            week.setType(sub.getTypeOfClasses());
                            String fragment = WeekDay.valueOf(sub.getDayOfWeek()).getDayName();
                            week.setFragment(fragment);
                            week.setBuilding(sub.getBuilding());
                            week.setRoom(sub.getRoom());
                            week.setColor(selected_color);
                            week.setFromTime(format24.format(sub.getStartTime()));
                            week.setToTime(format24.format(sub.getEndTime()));
                            dbHelper.insertWeek(week);

                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        usernameView.getText().clear();
                        apiKeyView.getText().clear();
                        usernameView.requestFocus();
                    } else {
                        Toast toast = Toast.makeText(activity, "Nie udało się pobrać planu zajęć.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        return dialog;
    }

    public static AlertDialog createClearTimetableDialog(Activity activity, View alertLayout, FragmentsTabAdapter adapter){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();

        Button cancel = alertLayout.findViewById(R.id.clear_timetable_cancel);
        Button clear = alertLayout.findViewById(R.id.clear_timetable);
        LinearLayout daysList = alertLayout.findViewById(R.id.checkbox_list);
        final int childCount = daysList.getChildCount();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper dbHelper = new DbHelper(activity);
                for(int i = 0; i < childCount; i++) {
                    CheckBox box = (CheckBox) daysList.getChildAt(i);
                    if(box.isChecked()) {
                        dbHelper.deleteWeeksByDay(WeekDay.valueOf(i + 1).getDayName());
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                for(int i = 0; i < childCount; i++) {
                    CheckBox box = (CheckBox) daysList.getChildAt(i);
                    box.setChecked(true);
                }
            }
        });

        return dialog;
    }

    public static AlertDialog createEnterPINDialog(Activity activity, View alertLayout, FragmentsTabAdapter adapter, String token, String secret_token){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();

        Button cancel = alertLayout.findViewById(R.id.usos_cancel);
        Button contin = alertLayout.findViewById(R.id.usos_continue);
        EditText pinText = alertLayout.findViewById(R.id.usos_pin);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        contin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(pinText.getText());
                dialog.dismiss();
                OAuthAuthenticator oAuthAuthenticator = new OAuthAuthenticator();
                //String uri = oAuthAuthenticator.generateOauthHeaderAccessToken("POST", token, secret_token, pinText.getText().toString());
                String uri = oAuthAuthenticator.generateOAuthUriForUSOS("POST", UsosApiPaths.ACCESS_TOKEN_PATH, new String[][]{{"oauth_token", token}, {"oauth_verifier", pinText.getText().toString()}}, secret_token);
                int timeout = 5;
                RequestConfig config = RequestConfig.custom()
                        .setConnectTimeout(timeout * 1000)
                        .setConnectionRequestTimeout(timeout * 1000).build();
                HttpPost request = new HttpPost(uri);
                try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
                    try (CloseableHttpResponse response = httpClient.execute(request)) {
                        if (response.getStatusLine().getStatusCode() == 200) {
                            HttpEntity entity = response.getEntity();
                            if (entity != null) {
                                String result = EntityUtils.toString(entity);
                                System.out.println("Result access: " + result);
                                String token = result.replace("oauth_token=", "");
                                int id = token.indexOf("&");
                                token = token.substring(0, id);
                                int start = result.indexOf("=", result.indexOf("=") + 1);
                                String secret_token = result.substring(start + 1);
                                //String uri_download_tt = oAuthAuthenticator.generateOauthHeaderDownloadTT("POST", token, secret_token);
                                String uri_download_tt = oAuthAuthenticator.generateOAuthUriForUSOS("POST", UsosApiPaths.STUDENT_TIMETABLE_PATH, new String[][]{{"oauth_token", token}}, secret_token);
                                HttpPost request_tt = new HttpPost(uri_download_tt);
                                try (CloseableHttpResponse response_tt = httpClient.execute(request_tt)) {
                                    if (response.getStatusLine().getStatusCode() == 200) {
                                        HttpEntity entity_tt = response_tt.getEntity();
                                        if (entity != null) {
                                            String result_tt = EntityUtils.toString(entity_tt);
                                            System.out.println("Result: " + result_tt);
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println(response.getStatusLine());
                            HttpEntity entity = response.getEntity();
                            System.out.println(EntityUtils.toString(entity));
                        }
                    }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        });

        return dialog;
    }
}
