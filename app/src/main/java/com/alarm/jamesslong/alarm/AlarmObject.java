package com.alarm.jamesslong.alarm;

import java.io.File;

/**
 * Created by jamesslong on 10/19/15.
 */
public class AlarmObject {
    Boolean alarmEnable = true;
    String alarmName = null;
    String alarmTime = null;
    Boolean alarmRepeat = false;
    Boolean alarmMath = false;
    Boolean alarmVibrate = false;
    File alarmTone = null;
    String[] daysOfWeek = new String[7];
}
