package com.alarm.jamesslong.alarm;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jamesslong on 10/19/15.
 */
public class AlarmObject {
    Boolean alarmEnable = true;
    String alarmName = null;
    String alarmTime = null;
    String alarmMethod = null;
    Boolean vibrate = false;
    File alarmTone = null;
    ArrayList<String> daysOfWeek = new ArrayList<>();
}
