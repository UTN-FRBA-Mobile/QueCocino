package com.mobile.utn.quecocino.timer;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobile.utn.quecocino.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimerCountdownFragment extends Fragment {

    @BindView(R.id.timerViewPager)
    public ViewPager viewPager;

    @BindView(R.id.timerPause)
    public Button pauseBtn;

    @BindView(R.id.timerStop)
    public Button stopBtn;

    @BindView(R.id.timerAdd)
    public Button addBtn;

    private Context thisContext;
    private Intent alarmIntent;
    private TimerViewPagerAdapter timerViewPagerAdapter;
    private List<TimerAlarm> alarms;
    private int currentPosition;

    public TimerCountdownFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = this.getActivity();
        alarmIntent = new Intent(thisContext,TimerAlarmReceiver.class);

        View view = inflater.inflate(R.layout.fragment_timer_countdown, container, false);
        ButterKnife.bind(this,view);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                TimerAlarm alarm = alarms.get(currentPosition);
                updateViewPager();

                if(alarm.isTimerRunning()){
                    pauseBtn.setText(R.string.timer_pause);
                } else {
                    pauseBtn.setText(R.string.timer_resume);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarms.get(currentPosition).isTimerRunning()) pauseTimer();
                else resumeTimer();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, new TimerEditFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        alarms = AlarmUtils.getAlarms(thisContext);
        currentPosition = alarms.size()-1;
        timerViewPagerAdapter = new TimerViewPagerAdapter(thisContext, alarms.size());
        viewPager.setAdapter(timerViewPagerAdapter);
        viewPager.post(new Runnable()
        {
            @Override
            public void run()
            {
                viewPager.setCurrentItem(currentPosition);
                updateViewPager();
            }
        });
    }

    private void updateViewPager(){
        TimerAlarm alarm = alarms.get(currentPosition);
        timerViewPagerAdapter.updatePage(alarm,currentPosition);
    }

    private void resumeTimer(){
        TimerAlarm alarm = alarms.get(currentPosition);
        AlarmUtils.resumeAlarm(thisContext,alarmIntent,alarm);
        timerViewPagerAdapter.startTimer();
        pauseBtn.setText(R.string.timer_pause);
    }

    public void pauseTimer() {
        TimerAlarm alarm = alarms.get(currentPosition);
        AlarmUtils.pauseAlarm(thisContext,alarmIntent,alarm);
        timerViewPagerAdapter.cancelTimer();
        pauseBtn.setText(R.string.timer_resume);
    }

    public void stopTimer() {
        AlarmUtils.cancelAlarm(thisContext,alarmIntent,alarms.get(currentPosition));
        if(timerViewPagerAdapter.getCount()<=1) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_content, new TimerEditFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            alarms.remove(currentPosition);
            currentPosition = timerViewPagerAdapter.removePage(viewPager,currentPosition);
            
        }
    }

}
