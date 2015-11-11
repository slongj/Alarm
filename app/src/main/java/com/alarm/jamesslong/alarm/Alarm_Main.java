package com.alarm.jamesslong.alarm;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Alarm_Main extends AppCompatActivity {
    static boolean DEBUG = false;
    ArrayList<AlarmObject> listOfAlarms = null;
    ListView listView = null;
    AlarmAdapter alarmAdapter = null;
    JSON_Write json_write = null;
    JSON_Read json_read = null;
    File file = null;
    OutputStream outputStream = null;
    InputStream inputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setContentView(R.layout.activity_alarm_main);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(listOfAlarms == null){
            init();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void init() {
        listView = (ListView) findViewById(R.id.alarmList);
        listOfAlarms = new ArrayList();
        FloatingActionButton addAlarmButton = (FloatingActionButton) findViewById(R.id.addAlarm);

        alarmAdapter = new AlarmAdapter(getApplicationContext(), listOfAlarms);
        listView.setAdapter(alarmAdapter);
        listView.setOnItemClickListener(toggleBottomPanel);
        addAlarmButton.setOnClickListener(addAlarm);

        if(DEBUG) {
            writeToJson();
        }
        readFromJson();
        alarmAdapter.notifyDataSetChanged();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(){
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion >=Build.VERSION_CODES.LOLLIPOP){
            Window w = getWindow();
            w.setStatusBarColor(getResources().getColor(R.color.android_dark_blue));
        }
    }


    AdapterView.OnItemClickListener toggleBottomPanel = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final AlarmObject alarm = listOfAlarms.get(position);
            RelativeLayout detailPanel = (RelativeLayout) view.findViewById(R.id.detailPanel);
            RelativeLayout simplePanel = (RelativeLayout) view.findViewById(R.id.simplePanel);

            toggleDetailPanel(alarm, position, detailPanel, simplePanel);
        }
    };


    public void toggleDetailPanel(final AlarmObject alarm, final int position, RelativeLayout detailPanel, RelativeLayout simplePanel){
        switch (detailPanel.getVisibility()) {
            case View.VISIBLE:
                panelInvisible(detailPanel, simplePanel);
                break;
            case View.GONE:
                panelVisible(alarm, detailPanel, simplePanel);
                break;
        }
    }


    View.OnClickListener addAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addAlarmToList();
            saveAndUpdate();
        }
    };

    public void panelInvisible(RelativeLayout detailPanel, RelativeLayout simplePanel){
        detailPanel.setVisibility(View.INVISIBLE);
        Helper_Animation.collapse(detailPanel);
        Helper_Animation.expand(simplePanel);
    }


    public void panelVisible(final AlarmObject alarm, RelativeLayout detailPanel, RelativeLayout simplePanel){
        final Resources resources = getResources();
        Helper_Animation.collapse(simplePanel);
        Helper_Animation.expand(detailPanel);

        final CheckBox alarmRepeatCheckBox = (CheckBox) detailPanel.findViewById(R.id.alarm_repeat);
        final CheckBox alarmMathCheckBox = (CheckBox) detailPanel.findViewById(R.id.alarm_math);
        final GridLayout alarmDaysOfWeekSelector = (GridLayout) detailPanel.findViewById(R.id.alarm_days_of_week_selector);
        final TextView alarmToneTextView = (TextView) detailPanel.findViewById(R.id.alarm_tone);
        final CheckBox alarmVibrateCheckBox = (CheckBox) detailPanel.findViewById(R.id.alarm_vibrate);

        alarmRepeatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.alarmRepeat = isChecked;
                saveAndUpdate();
            }
        });

        alarmMathCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.alarmMath = isChecked;
                saveAndUpdate();
            }
        });

        for(int i = 0; i < alarmDaysOfWeekSelector.getChildCount(); i++){
            TextView day = (TextView) alarmDaysOfWeekSelector.getChildAt(i);
            if(alarm.daysOfWeek[i] != null) {
                day.setTextColor(resources.getColor(R.color.android_blue));
                day.setBackgroundColor(resources.getColor(R.color.black));
            }
            day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    switch(tv.getId()){
                        case R.id.alarm_sunday:
                            if(tv.getCurrentTextColor() == resources.getColor(R.color.white)){
                                //unselected to selected
                                alarm.daysOfWeek[0] = "Sun";
                                tv.setTextColor(resources.getColor(R.color.android_blue));
                                tv.setBackgroundColor(resources.getColor(R.color.black));
                            }
                            else{
                                //selected to unselected
                                alarm.daysOfWeek[0] = null;
                                tv.setTextColor(resources.getColor(R.color.white));
                                tv.setBackgroundColor(resources.getColor(R.color.transparent));
                            }
                            break;
                        case R.id.alarm_monday:
                            if(tv.getCurrentTextColor() == resources.getColor(R.color.white)){
                                //unselected to selected
                                alarm.daysOfWeek[1] = "Mon";
                                tv.setTextColor(resources.getColor(R.color.android_blue));
                                tv.setBackgroundColor(resources.getColor(R.color.black));
                            }
                            else{
                                //selected to unselected
                                alarm.daysOfWeek[1] = null;
                                tv.setTextColor(resources.getColor(R.color.white));
                                tv.setBackgroundColor(resources.getColor(R.color.transparent));
                            }
                            break;
                        case R.id.alarm_tuesday:
                            if(tv.getCurrentTextColor() == resources.getColor(R.color.white)){
                                //unselected to selected
                                alarm.daysOfWeek[2] = "Tue";
                                tv.setTextColor(resources.getColor(R.color.android_blue));
                                tv.setBackgroundColor(resources.getColor(R.color.black));
                            }
                            else{
                                //selected to unselected
                                alarm.daysOfWeek[2] = null;
                                tv.setTextColor(resources.getColor(R.color.white));
                                tv.setBackgroundColor(resources.getColor(R.color.transparent));
                            }
                            break;
                        case R.id.alarm_wednesday:
                            if(tv.getCurrentTextColor() == resources.getColor(R.color.white)){
                                //unselected to selected
                                alarm.daysOfWeek[3] = "Wed";
                                tv.setTextColor(resources.getColor(R.color.android_blue));
                                tv.setBackgroundColor(resources.getColor(R.color.black));
                            }
                            else{
                                //selected to unselected
                                alarm.daysOfWeek[3] = null;
                                tv.setTextColor(resources.getColor(R.color.white));
                                tv.setBackgroundColor(resources.getColor(R.color.transparent));
                            }
                            break;
                        case R.id.alarm_thursday:
                            if(tv.getCurrentTextColor() == resources.getColor(R.color.white)){
                                //unselected to selected
                                alarm.daysOfWeek[4] = "Thu";
                                tv.setTextColor(resources.getColor(R.color.android_blue));
                                tv.setBackgroundColor(resources.getColor(R.color.black));
                            }
                            else{
                                //selected to unselected
                                alarm.daysOfWeek[4] = null;
                                tv.setTextColor(resources.getColor(R.color.white));
                                tv.setBackgroundColor(resources.getColor(R.color.transparent));
                            }
                            break;
                        case R.id.alarm_friday:
                            if(tv.getCurrentTextColor() == resources.getColor(R.color.white)){
                                //unselected to selected
                                alarm.daysOfWeek[5] = "Fri";
                                tv.setTextColor(resources.getColor(R.color.android_blue));
                                tv.setBackgroundColor(resources.getColor(R.color.black));
                            }
                            else{
                                //selected to unselected
                                alarm.daysOfWeek[5] = null;
                                tv.setTextColor(resources.getColor(R.color.white));
                                tv.setBackgroundColor(resources.getColor(R.color.transparent));
                            }
                            break;
                        case R.id.alarm_saturday:
                            if(tv.getCurrentTextColor() == resources.getColor(R.color.white)){
                                //unselected to selected
                                alarm.daysOfWeek[6] = "Sat";
                                tv.setTextColor(resources.getColor(R.color.android_blue));
                                tv.setBackgroundColor(resources.getColor(R.color.black));
                            }
                            else{
                                //selected to unselected
                                alarm.daysOfWeek[6] = null;
                                tv.setTextColor(resources.getColor(R.color.white));
                                tv.setBackgroundColor(resources.getColor(R.color.transparent));
                            }
                            break;
                    }
                    saveAndUpdate();
                }
            });
        }

        alarmVibrateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.alarmVibrate = isChecked;
                saveAndUpdate();
            }
        });
    }


    public void addAlarmToList(){
        DialogFragment timeDialog = new Dialog_AddAlarm_TimePicker();
        timeDialog.show(getFragmentManager(), "timePicker");



        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        //c.setTimeInMillis(System.currentTimeMillis());

        AlarmObject alarmObject = new AlarmObject();
        alarmObject.alarmEnable = true;
        alarmObject.alarmName = "";
        alarmObject.alarmTime = sdf.format(c.getTime());
        alarmObject.alarmRepeat = true;
        alarmObject.alarmMath = true;
        alarmObject.alarmVibrate = true;
        alarmObject.alarmTone = getFilesDir();
        alarmObject.daysOfWeek[c.get(Calendar.DAY_OF_WEEK) - 1] = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
        if(listOfAlarms != null) {
            listOfAlarms.add(alarmObject);
        }
    }


    public void saveAndUpdate(){
        writeToJson();
        alarmAdapter.notifyDataSetChanged();
    }


    public void writeToJson(){
        json_write = new JSON_Write();
        try {
            file = new File(getApplicationContext().getFilesDir(), "Alarms.json");
            outputStream = new FileOutputStream(file, false);
            json_write.writeAlarmToJSON(outputStream, listOfAlarms);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public void readFromJson(){
        json_read = new JSON_Read();
        try {
            file = new File(getApplicationContext().getFilesDir(), "Alarms.json");
            inputStream = new FileInputStream(file);
            listOfAlarms.clear();
            for(AlarmObject alarm : json_read.readAlarmsFromJSON(inputStream)) {
                listOfAlarms.add(alarm);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public class AlarmAdapter extends ArrayAdapter<AlarmObject> {
        public AlarmAdapter(Context context, ArrayList<AlarmObject> alarms){
            super(context, 0, alarms);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final AlarmObject alarm = getItem(position);
            String daysSelected = "";
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_object_main, parent, false);
            }

            TextView alarm_name = (TextView) convertView.findViewById(R.id.alarm_name);
            TextView alarm_time = (TextView) convertView.findViewById(R.id.alarm_time);
            final Switch alarm_enable = (Switch) convertView.findViewById(R.id.alarm_enable);
            TextView alarm_days_of_week = (TextView) convertView.findViewById(R.id.alarm_days_of_week);
            Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

            alarm_enable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarm.alarmEnable = alarm_enable.isChecked();
                    saveAndUpdate();
                }
            });


            if(alarm.alarmName.isEmpty()) {
                alarm_name.setVisibility(View.GONE);
            }
            else{
                alarm_name.setText(alarm.alarmName);
            }
            alarm_time.setText(alarm.alarmTime);
            alarm_time.setTypeface(tf);
            alarm_enable.setChecked(alarm.alarmEnable);
            int j = 0;
            for(int i = 0; i < (alarm.daysOfWeek.length); i++){
                if(alarm.daysOfWeek[i] != null) {
                    daysSelected += alarm.daysOfWeek[i] + " ";
                    j++;
                }
            }
            if(j == 7){
                daysSelected = "Everyday";
            }
            alarm_days_of_week.setText(daysSelected);
            return convertView;
        }
    }




















//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_alarm_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
