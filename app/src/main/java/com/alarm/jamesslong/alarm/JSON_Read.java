package com.alarm.jamesslong.alarm;

import android.util.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by jamesslong on 10/20/15.
 */
public class JSON_Read {
    public JSON_Read(){

    }


    public ArrayList<AlarmObject> readAlarmsFromJSON(InputStream in) throws IOException{
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try{
            return readAlarms(reader);
        }
        finally{
            reader.close();
        }
    }


    public ArrayList<AlarmObject> readAlarms(JsonReader reader) throws IOException{
        ArrayList<AlarmObject> listOfAlarms = new ArrayList<>();
        String cursor;
        reader.beginObject();
            while(reader.hasNext()) {
                cursor = reader.nextName();
                if (cursor.equals("alarms")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        AlarmObject alarm = readAlarm(reader);
                        if (alarm.alarmTime != null) {
                            listOfAlarms.add(alarm);
                        }
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
        reader.endObject();
        return listOfAlarms;
    }


    public AlarmObject readAlarm(JsonReader reader) throws IOException{
        AlarmObject alarm = new AlarmObject();
        String cursor;

        reader.beginObject();
        while(reader.hasNext()){
            cursor = reader.nextName();
            switch(cursor){
                case "alarmEnable":
                    alarm.alarmEnable = reader.nextBoolean();
                    break;
                case "alarmName":
                    alarm.alarmName = reader.nextString();
                    break;
                case "alarmTime":
                    alarm.alarmTime = reader.nextString();
                    break;
                case "alarmRepeat":
                    alarm.alarmRepeat = reader.nextBoolean();
                    break;
                case "alarmMath":
                    alarm.alarmMath = reader.nextBoolean();
                    break;
                case "alarmVibrate":
                    alarm.alarmVibrate = reader.nextBoolean();
                    break;
                case "alarmTone":
                    alarm.alarmTone = new File(reader.nextString());
                    break;
                case "daysOfWeek":
                    alarm.daysOfWeek = readAlarmDays(reader);
                    break;
            }
        }
        reader.endObject();
        return alarm;
    }


    public String[] readAlarmDays(JsonReader reader) throws IOException{
        String[] daysOfWeek = new String[7];
        reader.beginArray();
        for(int i = 0; i < 7; i++){
            daysOfWeek[i] = reader.nextString();
        }
        return daysOfWeek;
    }
}
