package com.mobile.utn.quecocino.timer;


import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.fragments.OnFragmentInteractionCollapse;
import com.mobile.utn.quecocino.menu.NavigationMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

public class TimerRingFragment extends Fragment {

    @BindView(R.id.timerRingTag)
    public TextView txtTag;
    @BindView(R.id.timerRingStop)
    public Button btnStop;

    private OnFragmentInteractionCollapse mListener;

    public TimerRingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer_ring, container, false);
        ButterKnife.bind(this,view);
        Context context = getContext();

        getActivity().setTitle(R.string.navigation_item_timers);

        Bundle args = getArguments();
        String alarmTag = (args!=null) ? args.getString("alarmTag") : "";
        txtTag.setText(alarmTag);

        final MediaPlayer mp = MediaPlayer.create(context, R.raw.timer_alarm_sound);
        mp.start();
        final Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                vibrator.cancel();
                TimerRingFragment.this.getActivity().finish();
            }
        });

        NavigationMenu activity = (NavigationMenu) getActivity();
        activity.showMenuItems(new ArrayList<Integer>());

        NotificationManager notificationmanager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(0);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.disableCollapse();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionCollapse) {
            mListener = (OnFragmentInteractionCollapse) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionCollapse");
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        Context thisContext = getContext();
        List<TimerAlarm> alarms = AlarmUtils.getAlarms(thisContext);
        int count = alarms.size();
        if(count==1){
            AlarmUtils.notifyTimer(thisContext,alarms.get(0));
        } else if (count>1) {
            AlarmUtils.notifyTimers(thisContext,count);
        }
    }
}
