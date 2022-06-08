package com.example.pwguide.utils;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.pwguide.R;
import com.example.pwguide.TimetableActivity;
import com.example.pwguide.adapters.FragmentsTabAdapter;
import com.example.pwguide.adapters.WeekAdapter;
import com.example.pwguide.fragments.FridayFragment;
import com.example.pwguide.fragments.MondayFragment;
import com.example.pwguide.fragments.SaturdayFragment;
import com.example.pwguide.fragments.SundayFragment;
import com.example.pwguide.fragments.ThursdayFragment;
import com.example.pwguide.fragments.TuesdayFragment;
import com.example.pwguide.fragments.WednesdayFragment;
import com.example.pwguide.model.Week;
import com.example.pwguide.model.WeekDay;
import com.example.pwguide.timetable_download.ISODTimetableDownload;
import com.example.pwguide.timetable_download.TimetableISOD;
import com.example.pwguide.timetable_download.USOSTimetableDownload;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlertDialogsHelper {

    public static void getEditSubjectDialog(final Activity activity, final View alertLayout, final ArrayList<Week> adapter, final ListView listView, int position) {
        final HashMap<Integer, EditText> editTextHashs = new HashMap<>();
        final EditText subject = alertLayout.findViewById(R.id.subject_dialog);
        editTextHashs.put(R.string.subject, subject);
        final Spinner class_type = alertLayout.findViewById(R.id.class_type_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(activity,
                R.array.class_types, R.layout.textview_to_spinner);
        spinnerAdapter.setDropDownViewResource(R.layout.textview_to_spinner);
        class_type.setAdapter(spinnerAdapter);
        final EditText building = alertLayout.findViewById(R.id.building_dialog);
        editTextHashs.put(R.string.building, building);
        final EditText room = alertLayout.findViewById(R.id.room_dialog);
        editTextHashs.put(R.string.room, room);
        final TextView from_time = alertLayout.findViewById(R.id.from_time);
        final TextView to_time = alertLayout.findViewById(R.id.to_time);
        final Button select_color = alertLayout.findViewById(R.id.select_color);
        final Week week = adapter.get(position);

        subject.setText(week.getSubject());
        building.setText(week.getBuilding());
        ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(activity,
                R.array.class_types_abbr, R.layout.textview_to_spinner);
        class_type.setSelection(spinnerAdapter2.getPosition(week.getType()));
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
                        select_color.setText("Kolor wybrany");
                        System.out.println(color);
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
                    week.setType(class_type.getSelectedItem().toString());
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
        final Spinner class_type = alertLayout.findViewById(R.id.class_type_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(activity,
                R.array.class_types, R.layout.textview_to_spinner);
        spinnerAdapter.setDropDownViewResource(R.layout.textview_to_spinner);
        class_type.setAdapter(spinnerAdapter);
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
                        select_color.setText("Kolor wybrany");
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
                    week.setType(class_type.getSelectedItem().toString());
                    week.setFragment(fragment.find() ? fragment.group().replace("Fragment", "") : null);
                    week.setBuilding(building.getText().toString());
                    week.setRoom(room.getText().toString());
                    week.setColor(buttonColor.getColor());
                    dbHelper.insertWeek(week);
                    adapter.notifyDataSetChanged();
                    subject.getText().clear();
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

        List<String> sems = getPossibleSemesters();
        final Spinner semesterSpinner = alertLayout.findViewById(R.id.isod_timetable_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(activity, R.layout.textview_to_spinner, sems);
        spinnerAdapter.setDropDownViewResource(R.layout.textview_to_spinner);
        semesterSpinner.setAdapter(spinnerAdapter);
        semesterSpinner.setSelection(spinnerAdapter.getPosition(sems.get(1)));

        Button cancel = alertLayout.findViewById(R.id.timetable_cancel);
        Button download = alertLayout.findViewById(R.id.timetable_download);
        EditText usernameView = alertLayout.findViewById(R.id.timetable_username);
        EditText apiKeyView = alertLayout.findViewById(R.id.timetable_apiKey);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                usernameView.getText().clear();
                apiKeyView.getText().clear();
                usernameView.requestFocus();
                semesterSpinner.setSelection(spinnerAdapter.getPosition(sems.get(1)));
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameView.getText().toString();
                String apiKey = apiKeyView.getText().toString();

                ISODTimetableDownload timetableDownload = new ISODTimetableDownload();
                try {
                    String semester = semesterSpinner.getSelectedItem().toString();
                    TimetableISOD timetableISOD = timetableDownload.downloadTimetableFromISOD(username, apiKey, semester);
                    if(timetableISOD != null) {
                        Random random = new Random();
                        SimpleDateFormat format24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        for (TimetableISOD.Subject sub: timetableISOD.getPlanItems()
                        ) {
                            final Week week = new Week();
                            DbHelper dbHelper = new DbHelper(activity);
                            int[] mColors = activity.getResources().getIntArray(R.array.default_colors);
                            int no_color = random.nextInt(mColors.length);
                            int selected_color = mColors[no_color];
                            week.setSubject(sub.getCourseName());
                            week.setType(sub.getTypeOfClasses());
                            String fragment = WeekDay.valueOf(sub.getDayOfWeek()).getDayNameEN();
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
                        semesterSpinner.setSelection(spinnerAdapter.getPosition(sems.get(1)));
                        usernameView.requestFocus();

                        Toast toast = Toast.makeText(activity, "Plan zajęć został pobrany.", Toast.LENGTH_SHORT);
                        toast.show();
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

    private static List<String> getPossibleSemesters() {
        List<String> spinnerArray =  new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        if( month < 8 && month > 0) {
            spinnerArray.add(year + "Z");
            spinnerArray.add(year + "L");
            spinnerArray.add((year-1) + "Z");
        } else if(month > 7){
            spinnerArray.add((year+1) + "L");
            spinnerArray.add(year + "Z");
            spinnerArray.add(year + "L");
        } else {
            spinnerArray.add(year + "L");
            spinnerArray.add((year-1) + "Z");
            spinnerArray.add((year-1) + "L");
        }
        return spinnerArray;
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
                        dbHelper.deleteWeeksByDay(WeekDay.valueOf(i + 1).getDayNameEN());
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

    public static AlertDialog createSelectDaysToDisplayDialog(Activity activity, View alertLayout, FragmentsTabAdapter adapter, SharedPreferences sharedPreferences){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();

        Button cancel = alertLayout.findViewById(R.id.days_display_cancel);
        Button save = alertLayout.findViewById(R.id.days_display_save);
        LinearLayout daysList = alertLayout.findViewById(R.id.days_display_checkbox_list);
        final int childCount = daysList.getChildCount();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < childCount; i++) {
                    CheckBox box = (CheckBox) daysList.getChildAt(i);
                    if(!box.isChecked()) {
                        removeDisplayedDay(box.getText().toString(), adapter, sharedPreferences);
                    } else {
                        addDisplayedDay(box.getText().toString(), adapter, sharedPreferences);
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Set<String> activeDays = new HashSet<>();
                Set<String> activeDaysPref = sharedPreferences.getStringSet(TimetableActivity.DISPLAYED_DAYS_PREFERENCE, activeDays);
                for(int i = 0; i < childCount; i++) {
                    CheckBox box = (CheckBox) daysList.getChildAt(i);
                    box.setChecked(activeDaysPref.contains(box.getText().toString()));
                }
            }
        });

        return dialog;
    }

    private static void addDisplayedDay(String day, FragmentsTabAdapter adapter, SharedPreferences preferences) {
        switch (day) {
            case "Poniedziałek":
                addDay(day, adapter, preferences, new MondayFragment());
                break;
            case "Wtorek":
                addDay(day, adapter, preferences, new TuesdayFragment());
                break;
            case "Środa":
                addDay(day, adapter, preferences, new WednesdayFragment());
                break;
            case "Czwartek":
                addDay(day, adapter, preferences, new ThursdayFragment());
                break;
            case "Piątek":
                addDay(day, adapter, preferences, new FridayFragment());
                break;
            case "Sobota":
                addDay(day, adapter, preferences, new SaturdayFragment());
                break;
            case "Niedziela":
                addDay(day, adapter, preferences, new SundayFragment());
                break;
            default:
                break;
        }
    }

    private static void addDay(String day, FragmentsTabAdapter adapter, SharedPreferences preferences, Fragment fragment) {
        Set<String> activeDays = new HashSet<>();
        Set<String> activeDaysPref = preferences.getStringSet(TimetableActivity.DISPLAYED_DAYS_PREFERENCE, activeDays);
        if(!activeDaysPref.contains(day)) {
            adapter.addFragment(fragment, day);
            SharedPreferences.Editor editor = preferences.edit();
            activeDays.addAll(activeDaysPref);
            activeDays.add(day);
            editor.putStringSet(TimetableActivity.DISPLAYED_DAYS_PREFERENCE, activeDays);
            editor.apply();
        }
    }

    private static void removeDisplayedDay(String day, FragmentsTabAdapter adapter, SharedPreferences preferences) {
        Set<String> activeDays = new HashSet<>();
        Set<String> activeDaysPref = preferences.getStringSet(TimetableActivity.DISPLAYED_DAYS_PREFERENCE, activeDays);
        adapter.removeFragment(day);
        SharedPreferences.Editor editor = preferences.edit();
        activeDays.addAll(activeDaysPref);
        activeDays.remove(day);
        editor.putStringSet(TimetableActivity.DISPLAYED_DAYS_PREFERENCE, activeDays);
        editor.apply();
    }


    public static AlertDialog createEnterPINDialog(Activity activity, View alertLayout, FragmentsTabAdapter adapter, String requestToken, String secretRequestToken){
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
                USOSTimetableDownload.downloadTimetableFromUSOS(activity, adapter, dialog, requestToken, pinText.getText().toString(), secretRequestToken);
            }
        });

        return dialog;
    }
}
