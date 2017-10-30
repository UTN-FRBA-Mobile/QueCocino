package com.mobile.utn.quecocino.timer;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.utn.quecocino.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.VIBRATOR_SERVICE;

public class TimerRingFragment extends Fragment {

    @BindView(R.id.timerRingTag)
    public TextView txtTag;
    @BindView(R.id.timerRingStop)
    public Button btnStop;

    public TimerRingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer_ring, container, false);
        ButterKnife.bind(this,view);
        Context context = getContext();

        Bundle args = getArguments();
        String alarmTag = (args!=null) ? args.getString("alarmTag") : "";
        txtTag.setText(alarmTag);

        final MediaPlayer mp = MediaPlayer.create(context, R.raw.timer_alarm_sound);
        mp.start();
        final Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        final Fragment that = this;
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                vibrator.cancel();
                that.getActivity().finish();
            }
        });

        return view;
    }

}
