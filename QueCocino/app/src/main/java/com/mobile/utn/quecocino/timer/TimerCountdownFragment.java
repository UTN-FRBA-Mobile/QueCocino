package com.mobile.utn.quecocino.timer;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.menu.NavigationMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.NOTIFICATION_SERVICE;

public class TimerCountdownFragment extends Fragment {

    @BindView(R.id.timerViewPager)
    public ViewPager viewPager;

    @BindView(R.id.timerPause)
    public Button pauseBtn;

    @BindView(R.id.timerStop)
    public Button stopBtn;

    @BindView(R.id.timerAdd)
    public Button addBtn;

    @BindView(R.id.timerViewPagerDots)
    public LinearLayout dotsPanel;
    private List<ImageView> dots;

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
        thisContext = this.getContext();
        alarmIntent = new Intent(thisContext,TimerAlarmReceiver.class);

        View view = inflater.inflate(R.layout.fragment_timer_countdown, container, false);
        ButterKnife.bind(this,view);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                updateViewPager();
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
                Fragment fr = new TimerEditFragment();
                Bundle args = new Bundle();
                args.putBoolean("isAddTimer",true);
                fr.setArguments(args);
                fragmentTransaction.replace(R.id.navigation_container,fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        alarms = AlarmUtils.getAlarms(thisContext);

        Bundle args = getArguments();
        if(args!=null){
            String action = (args.containsKey("timerAction")) ? args.getString("timerAction") : "";
            int alarmId = (args.containsKey("alarmId")) ? args.getInt("alarmId") : 0;
            if(action.equals("pause")) {
                TimerAlarm pausedAlarm = null;
                for(TimerAlarm alarm : alarms){
                    if(alarm.getId()==alarmId){
                        pausedAlarm = alarm;
                        break;
                    }
                }
                if(pausedAlarm!=null){
                    AlarmUtils.pauseAlarm(thisContext,alarmIntent,pausedAlarm);
                }
            } else if (action.equals("stop")) {
                AlarmUtils.cancelAlarm(thisContext,alarmIntent,alarmId);
                for(TimerAlarm alarm : alarms){
                    if(alarm.getId()==alarmId){
                        alarms.remove(alarm);
                        break;
                    }
                }
            }
        }
        NotificationManager notificationmanager = (NotificationManager) thisContext.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(0);

        if(alarms.size()>0) {
            dots = new ArrayList<>();
            dotsPanel.removeAllViews();
            for(int i = 0; i < alarms.size(); i++){
                ImageView imgView = new ImageView(thisContext);
                imgView.setImageDrawable(ContextCompat.getDrawable(thisContext, R.drawable.nonactive_dot));
                dots.add(imgView);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 8, 0, 8);
                dotsPanel.addView(imgView, params);
            }

            timerViewPagerAdapter = new TimerViewPagerAdapter(thisContext, alarms.size());
            viewPager.setAdapter(timerViewPagerAdapter);
            viewPager.setOffscreenPageLimit(alarms.size());
            currentPosition = alarms.size()-1;
            viewPager.post(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(currentPosition, false);
                    if(currentPosition==0) updateViewPager();

                }
            });
        } else {
            finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        int count = 0;
        TimerAlarm runningAlarm = null;
        for(TimerAlarm alarm : alarms) {
            if(alarm.isTimerRunning()) {
                count++;
                runningAlarm = alarm;
            }
        }
        if(count==1){
            notifyTimer(runningAlarm.getId(),runningAlarm.getTag());
        } else if (count>1) {
            notifyTimers(count);
        }
    }

    private void updateViewPager(){
        TimerAlarm alarm = alarms.get(currentPosition);
        timerViewPagerAdapter.updatePage(alarm,currentPosition);
        if(alarm.isTimerRunning()){
            pauseBtn.setText(R.string.timer_pause);
        } else {
            pauseBtn.setText(R.string.timer_resume);
        }
        for(int i = 0; i< dots.size(); i++){
            dots.get(i).setImageDrawable(ContextCompat.getDrawable(thisContext, R.drawable.nonactive_dot));
        }
        dots.get(currentPosition).setImageDrawable(ContextCompat.getDrawable(thisContext, R.drawable.active_dot));
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
        AlarmUtils.cancelAlarm(thisContext,alarmIntent,alarms.get(currentPosition).getId());
        alarms.remove(currentPosition);
        View viewToDelete = timerViewPagerAdapter.getView(currentPosition);
        viewToDelete.animate()
                    .setDuration(500)
                    .alpha(0)
                    .scaleXBy(-0.5f)
                    .scaleYBy(-0.5f);
        if(timerViewPagerAdapter.getCount()<=1) {
            viewToDelete.animate()
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    })
                    .start();
        } else {
            viewToDelete.animate()
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                View dot = dots.get(currentPosition);
                                dotsPanel.removeView(dot);
                                dots.remove(currentPosition);
                                currentPosition = timerViewPagerAdapter.removePage(viewPager,currentPosition);
                                updateViewPager();

                            }
                        })
                        .start();
        }
    }

    private void finish() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.replace(R.id.navigation_container, new TimerEditFragment());
        fragmentTransaction.commit();
    }

    private void notifyTimers(int count) {
        Intent intent = new Intent(thisContext, NavigationMenu.class);
        intent.putExtra("fragment", "timerCountdown");
        PendingIntent contentPI = PendingIntent.getActivity(thisContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(thisContext)
                .setSmallIcon(R.drawable.recipe_cookingtime)
                .setTicker("Timer Running")
                .setContentTitle(getString(R.string.timer_notification_title_plural).replace("#",String.valueOf(count)))
                .setColor(ContextCompat.getColor(thisContext,R.color.colorPrimaryDark))
                .setContentIntent(contentPI)
                .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) thisContext.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }

    private void notifyTimer(int alarmId, String tag) {
        PendingIntent contentPI = buildPendingIntent(thisContext,alarmId, alarmId, "");
        PendingIntent pausePI = buildPendingIntent(thisContext,alarmId+1, alarmId, "pause");
        PendingIntent stopPI = buildPendingIntent(thisContext,alarmId+2, alarmId,"stop");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(thisContext)
                .setSmallIcon(R.drawable.recipe_cookingtime)
                .setTicker("Timer Running")
                .setContentTitle(getString(R.string.timer_notification_title))
                .addAction(R.drawable.ic_pause_white, getString(R.string.timer_pause), pausePI)
                .addAction(R.drawable.ic_stop_white, getString(R.string.timer_stop), stopPI)
                .setColor(ContextCompat.getColor(thisContext,R.color.colorPrimaryDark))
                .setContentIntent(contentPI)
                .setAutoCancel(true);

        if(!tag.isEmpty()) {
            builder.setContentText(tag);
        }

        NotificationManager notificationmanager = (NotificationManager) thisContext.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }

    private PendingIntent buildPendingIntent(Context context, int requestId, int alarmId, String action){
        Intent intent = new Intent(context, NavigationMenu.class);
        intent.putExtra("fragment", "timerCountdown");
        intent.putExtra("timerAction",action);
        intent.putExtra("alarmId",alarmId);
        return PendingIntent.getActivity(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
