package com.mobile.utn.quecocino.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TimerAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Riiiing!", Toast.LENGTH_LONG).show();
    }

}
