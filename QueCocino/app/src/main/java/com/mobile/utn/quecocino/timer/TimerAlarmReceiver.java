package com.mobile.utn.quecocino.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.menu.NavigationMenu;

import static android.content.Context.VIBRATOR_SERVICE;

public class TimerAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle args = intent.getExtras();
        AlarmUtils.removeAlarm(context,args.getInt("alarmId"));
        Intent newIntent = new Intent(context, TimerRingFragment.class);
        newIntent.putExtras(args);
        context.startActivity(newIntent);
    }

}
