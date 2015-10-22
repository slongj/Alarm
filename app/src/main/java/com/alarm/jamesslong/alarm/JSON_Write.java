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

        writer.beginObject();
            writer.name("alarms");
            writer.beginArray();
            for(AlarmObject alarm : listOfAlarms){
                writeAlarm(writer, alarm);
                sizeCount++;
            }
            writer.endArray();
            writer.name("size").value(sizeCount);
        writer.endObject();
        writer.close();
    }


    public void writeAlarm(JsonWriter writer, AlarmObject alarm) throws IOException{
        writer.beginObject();
        writer.name("alarmEnable").value(alarm.alarmEnable);
        writer.name("alarmName").value(alarm.alarmName);
        writer.name("alarmTime").value(alarm.alarmTime);
        writer.name("alarmRepeat").value(alarm.alarmRepeat);
        writer.name("alarmMath").value(alarm.alarmMath);
        writer.name("alarmVibrate").value(alarm.alarmVibrate);
        writer.name("alarmTone").value(alarm.alarmTone.toString());
        writeAlarmDays(writer, alarm.daysOfWeek);
        writer.endObject();
    }


    public void writeAlarmDays(JsonWriter writer, String[] daysOfWeek) throws IOException{
        writer.name("daysOfWeek");
        writer.beginArray();
        for(String day : daysOfWeek){
            writer.value(day);
        }
        writer.endArray();
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
