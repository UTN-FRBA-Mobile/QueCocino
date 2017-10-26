package com.mobile.utn.quecocino.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.Toast;

import com.mobile.utn.quecocino.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class TimerAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.timer_alarm_sound);
        mp.start();
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
        Toast.makeText(context, "Riiiing!", Toast.LENGTH_LONG).show();
    }

}
