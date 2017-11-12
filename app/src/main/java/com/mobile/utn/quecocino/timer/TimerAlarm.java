package com.mobile.utn.quecocino.timer;

public class TimerAlarm {
    private int id;
    private long time;
    private String tag;
    private boolean isTimerRunning;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        isTimerRunning = timerRunning;
    }

    public int getSeconds() {
        long millis;
        if (isTimerRunning) {
            millis= time - System.currentTimeMillis();
        } else {
            millis = time;
        }
        return (int) Math.ceil((millis+1000) / 1000.0);
    }
}
