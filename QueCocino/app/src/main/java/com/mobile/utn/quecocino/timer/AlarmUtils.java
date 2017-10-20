package com.mobile.utn.quecocino.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.util.LongSparseArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmUtils {

    public static String sTagAlarms = ":alarms";

    public static void addAlarm(Context context, Intent intent, Long millis, String tag) {

        String alarmId = Long.toString(millis);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        }

        saveAlarmData(context, alarmId, tag);
    }

    public static void cancelAlarm(Context context, Intent intent, String alarmId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        removeAlarmData(context, alarmId);
    }

    public static void cancelAllAlarms(Context context, Intent intent) {
        for (String idAlarm : getAlarmIds(context)) {
            cancelAlarm(context, intent, idAlarm);
        }
    }

    public static boolean hasAlarm(Context context, Intent intent) {
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    public static boolean hasAlarms(Context context) {
        return getAlarmIds(context).size() > 0;
    }

    public static LongSparseArray<String> getAlarmsData(Context context) {
        LongSparseArray<String> alarmsData = new LongSparseArray<>();
        List<String> alarmIds = getAlarmIds(context);
        for (String alarmId : alarmIds) {
            alarmsData.append(Long.parseLong(alarmId),getAlarmTag(context,alarmId));
        }
        return alarmsData;
    }

    private static void saveAlarmData(Context context, String alarmId, String tag) {
        List<String> idsAlarms = getAlarmIds(context);
        if (idsAlarms.contains(alarmId)) {
            return;
        }
        idsAlarms.add(alarmId);
        saveIdsInPreferences(context, idsAlarms);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getPackageName() + ":" + alarmId, tag);
        editor.apply();
    }

    private static void removeAlarmData(Context context, String alarmId) {
        List<String> idsAlarms = getAlarmIds(context);
        for (int i = 0; i < idsAlarms.size(); i++) {
            if (idsAlarms.get(i) == alarmId)
                idsAlarms.remove(i);
        }
        saveIdsInPreferences(context, idsAlarms);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(context.getPackageName() + ":" + alarmId);
        editor.apply();
    }

    private static List<String> getAlarmIds(Context context) {
        List<String> ids = new ArrayList<>();
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            JSONArray jsonArray2 = new JSONArray(prefs.getString(context.getPackageName() + sTagAlarms, "[]"));

            for (int i = 0; i < jsonArray2.length(); i++) {
                ids.add(jsonArray2.getString(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ids;
    }

    private static String getAlarmTag(Context context, String alarmId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getPackageName() + ":" + alarmId, "");
    }

    private static void saveIdsInPreferences(Context context, List<String> lstIds) {
        JSONArray jsonArray = new JSONArray();
        for (String idAlarm : lstIds) {
            jsonArray.put(idAlarm);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getPackageName() + sTagAlarms, jsonArray.toString());

        editor.apply();
    }
}