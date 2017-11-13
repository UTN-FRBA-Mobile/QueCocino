package com.mobile.utn.quecocino.timer;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LongSparseArray;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.menu.NavigationMenu;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public abstract class AlarmUtils {

    public static String sTagAlarms = ":alarms";

    public static int addAlarm(Context context, Intent intent, Long time, String tag) {
        TimerAlarm alarm = new TimerAlarm();
        alarm.setId((int) ((time / 1000L) % Integer.MAX_VALUE));
        alarm.setTime(time);
        alarm.setTag(tag);
        alarm.setTimerRunning(true);

        intent.putExtra("fragment","timerRing");
        intent.putExtra("alarmTag",alarm.getTag());
        intent.putExtra("alarmId",alarm.getId());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }

        saveAlarm(context, alarm);

        return alarm.getId();
    }

    public static void pauseAlarm(Context context, Intent intent, TimerAlarm alarm) {
        Long time = alarm.getTime()-System.currentTimeMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(context.getPackageName() + ":time:" + alarm.getId(), time);
        editor.putBoolean(context.getPackageName() + ":running:" + alarm.getId(), false);
        editor.apply();

        alarm.setTime(time);
        alarm.setTimerRunning(false);
    }

    public static void resumeAlarm(Context context, Intent intent, TimerAlarm alarm) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Long time = alarm.getTime();
        time += System.currentTimeMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(context.getPackageName() + ":time:" + alarm.getId(), time);
        editor.putBoolean(context.getPackageName() + ":running:" + alarm.getId(), true);
        editor.apply();

        alarm.setTime(time);
        alarm.setTimerRunning(true);
    }

    public static void cancelAlarm(Context context, Intent intent, int alarmId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        removeAlarm(context, alarmId);
    }

    public static boolean hasAlarm(Context context, Intent intent, int alarmId) {
        return PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    public static boolean hasAlarms(Context context) {
        List<String> alarmIds = getAlarmIds(context);
        return alarmIds.size() > 0;
    }

    public static List<TimerAlarm> getAlarms(Context context) {
        List<TimerAlarm> alarms = new ArrayList<>() ;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        List<String> alarmIds = getAlarmIds(context);
        for (String alarmStrId : alarmIds) {
            TimerAlarm alarm = new TimerAlarm();
            alarm.setId(Integer.parseInt(alarmStrId));
            alarm.setTime(prefs.getLong(context.getPackageName() + ":time:" + alarmStrId, 0));
            alarm.setTag(prefs.getString(context.getPackageName() + ":tag:" + alarmStrId, ""));
            alarm.setTimerRunning(prefs.getBoolean(context.getPackageName() + ":running:" + alarmStrId, true));
            alarms.add(alarm);
        }

        return alarms;
    }

    private static void saveAlarm(Context context, TimerAlarm alarm) {
        String alarmStrId = Integer.toString(alarm.getId());

        List<String> idsAlarms = getAlarmIds(context);
        if (idsAlarms.contains(alarmStrId)) {
            return;
        }
        idsAlarms.add(alarmStrId);
        saveIdsInPreferences(context, idsAlarms);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(context.getPackageName() + ":time:" + alarmStrId, alarm.getTime());
        editor.putString(context.getPackageName() + ":tag:" + alarmStrId, alarm.getTag());
        editor.putBoolean(context.getPackageName() + ":running:" + alarmStrId, alarm.isTimerRunning());
        editor.apply();
    }

    public static void removeAlarm(Context context, int alarmId) {
        String alarmStrId = Integer.toString(alarmId);

        List<String> idsAlarms = getAlarmIds(context);
        for (int i = 0; i < idsAlarms.size(); i++) {
            if (idsAlarms.get(i).equals(alarmStrId))
                idsAlarms.remove(i);
        }
        saveIdsInPreferences(context, idsAlarms);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(context.getPackageName() + ":time:" + alarmStrId);
        editor.remove(context.getPackageName() + ":tag:" + alarmStrId);
        editor.remove(context.getPackageName() + ":running:" + alarmStrId);
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

    public static void notifyTimers(Context context, int count) {
        Intent intent = new Intent(context, NavigationMenu.class);
        intent.putExtra("fragment", "timerCountdown");
        PendingIntent contentPI = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.recipe_cookingtime)
                .setTicker("Timer Running")
                .setContentTitle(context.getString(R.string.timer_notification_title_plural).replace("#",String.valueOf(count)))
                .setColor(ContextCompat.getColor(context,R.color.colorPrimaryDark))
                .setContentIntent(contentPI)
                .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }

    public static void notifyTimer(Context context, TimerAlarm alarm) {
        int alarmId = alarm.getId();
        PendingIntent contentPI = buildPendingIntent(context, alarmId, alarmId, "");
        PendingIntent pausePI = buildPendingIntent(context,alarmId+1, alarmId, "pause");
        PendingIntent stopPI = buildPendingIntent(context,alarmId+2, alarmId,"stop");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.recipe_cookingtime)
                .setTicker("Timer Running")
                .setContentTitle(context.getString(R.string.timer_notification_title))
                .addAction(R.drawable.ic_pause_white, context.getString(R.string.timer_pause), pausePI)
                .addAction(R.drawable.ic_stop_white, context.getString(R.string.timer_stop), stopPI)
                .setColor(ContextCompat.getColor(context,R.color.colorPrimaryDark))
                .setContentIntent(contentPI)
                .setWhen(alarm.getTime())
                .setAutoCancel(true);

        if(!alarm.getTag().isEmpty()) {
            builder.setContentText(alarm.getTag());
        }

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }

    private static PendingIntent buildPendingIntent(Context context, int requestId, int alarmId, String action){
        Intent intent = new Intent(context, NavigationMenu.class);
        intent.putExtra("fragment", "timerCountdown");
        intent.putExtra("timerAction",action);
        intent.putExtra("alarmId",alarmId);
        return PendingIntent.getActivity(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}