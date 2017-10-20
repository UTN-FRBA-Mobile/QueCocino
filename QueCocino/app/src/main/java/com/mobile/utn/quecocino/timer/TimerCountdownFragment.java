package com.mobile.utn.quecocino.timer;


import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.support.v4.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.utn.quecocino.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimerCountdownFragment extends Fragment {

    @BindView(R.id.timerTitle)
    public TextView title;

    @BindView(R.id.timerCountdown)
    public TextView countdown;

    @BindView(R.id.timerPause)
    public Button pauseBtn;

    @BindView(R.id.timerStop)
    public Button stopBtn;

    @BindView(R.id.timerAdd)
    public Button addBtn;

    private int hh;
    private int mm;
    private int ss;
    private CountDownTimer cdt;

    public TimerCountdownFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer_countdown, container, false);
        ButterKnife.bind(this,view);

        LongSparseArray<String> alarmsData = AlarmUtils.getAlarmsData(this.getActivity());

        for(int i = 0; i < alarmsData.size(); i++) {
            long key = alarmsData.keyAt(i);
            long millis = key - System.currentTimeMillis();
            String value = alarmsData.get(key);

            //FIX this
            int time = (int) (millis / 1000);
            hh = time / 3600;
            time = time % 3600;
            mm= time / 60;
            time = time % 60;
            ss = time;
            title.setText(value);
        }

        cdt = createCDT();
        cdt.start();

        return view;
    }

    private CountDownTimer createCDT() {
        return new CountDownTimer((hh*3600+mm*60+ss-1)*1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int) (millisUntilFinished / 1000);
                hh = time / 3600;
                time = time % 3600;
                mm= time / 60;
                time = time % 60;
                ss = time;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                hh = mm = ss = 0;
                updateCountdownText();
                stopTimer();
            }

        };
    }

    public void stopTimer() {
        cdt.cancel();
    }

    private String pad(int num){
        String str = Integer.toString(num);
        if(str.length()==1) str = "0" + str;
        return str;
    }

    private void updateCountdownText() {
        countdown.setText(pad(hh)+":"+pad(mm)+":"+pad(ss));
    }

}
