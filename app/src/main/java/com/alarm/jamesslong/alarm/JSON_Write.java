package com.alarm.jamesslong.alarm;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by jamesslong on 10/20/15.
 */
public class JSON_Write {
    public JSON_Write(){

    }

    public void writeAlarmToJSON(OutputStream out, ArrayList<AlarmObject> listOfAlarms) throws IOException{
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("   ");
        int sizeCount = 0;
    }

//    public static void writeToJSON(Context context, ArrayList<AlarmObject> listOfAlarms){
//        Resources resources = context.getResources();
//        String[] days_of_week = resources.getStringArray(R.array.days_of_week);
//
//        listOfAlarms.clear();
//        boolean bool = false;
//        for(int i = 0; i < 5; i++) {
//            JSONObject alarm = new JSONObject();
//            JSONArray daysOfWeek = new JSONArray();
//            try {
//                if (bool) {
//                    alarm.put("alarmName", "Alarm" + i);
//                }
//                alarm.put("alarmTime", "08:0"+i);
//                alarm.put("alarmMethod", "Simple");
//                alarm.put("alarmEnable", bool);
//                alarm.put("alarmVibrate", bool);
//                daysOfWeek.put(days_of_week[i]);
//                alarm.put("daysOfWeek", daysOfWeek);
//            }
//            catch(JSONException e){
//                System.out.println("JSONException from JSON_Write" + e);
//            }
//
//            bool = !bool;
//        }
//    }
}
