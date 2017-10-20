package com.mobile.utn.quecocino.timer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.mobile.utn.quecocino.R;

import java.util.Random;

public class TimerService {
/*
    public class TimerServiceBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    private IBinder timerServiceBinder = new TimerServiceBinder();
    private TimerObserver timerObserver;

    private boolean timerRunning = false;
    private int hh = 0;
    private int mm = 0;
    private int ss = 0;
    private CountDownTimer cdt;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return timerServiceBinder;
    }

    public boolean isTimerRunning(){
        return timerRunning;
    }

    public String getTime(){
        return String.format("%02d", hh) + String.format("%02d", mm) + String.format("%02d", ss);
    }

    public void registrateObserver(TimerObserver timerObserver){
        this.timerObserver = timerObserver;
    }

    public void unregistrateObserver(){
        this.timerObserver = null;
    }

    public void runTimer(int hh, int mm, int ss) {
        this.hh = hh;
        this.mm = mm;
        this.ss = ss;
        cdt = createCDT();
        timerRunning = true;
        cdt.start();
    }

    public void stopTimer() {
        if(timerRunning){
            cdt.cancel();
            timerRunning = false;
            stopForeground(true);
        }
    }

    public void startAsForeground(){
        int notificationID = new Random().nextInt();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("QueCocino");
        mBuilder.setContentText("Timer Running...");
        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, TimerEditFragment.class), 0);
        mBuilder.setContentIntent(intent);
        startForeground(notificationID, mBuilder.build());
    }


*/
}
