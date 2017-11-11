package com.mobile.utn.quecocino.timer;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.util.Pair;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.utn.quecocino.R;

import java.util.ArrayList;
import java.util.List;

public class TimerViewPagerAdapter extends PagerAdapter {

    private List<View> pages;

    public TextView title;
    public TextView countdown;

    private int hh;
    private int mm;
    private int ss;
    private CountDownTimer cdt;

    public TimerViewPagerAdapter(Context context, int count) {
        this.pages = new ArrayList<>();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i=0; i<count; i++) {
            View view = layoutInflater.inflate(R.layout.timer_viewpager_countdown,null);
            pages.add(view);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = pages.get(position);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);
        return view;
    }

    public int removePage(ViewGroup container, int position){
        ViewPager viewPager = (ViewPager) container;
        viewPager.removeView(pages.get(position));
        pages.remove(position);
        if (position == this.getCount()){
            position--;
        }
        notifyDataSetChanged();
        return position;
    }

    public void updatePage(TimerAlarm alarm, int position){
        View currentView = pages.get(position);
        countdown = (TextView) currentView.findViewById(R.id.timerCountdown);
        title = (TextView) currentView.findViewById(R.id.timerTitle);

        if(cdt != null) cancelTimer();

        int time = alarm.getSeconds();
        hh = time / 3600;
        time = time % 3600;
        mm= time / 60;
        time = time % 60;
        ss = time;
        updateCountdownText();
        if(alarm.isTimerRunning()) startTimer();

        String value = alarm.getTag();
        title.setText(value);
    }

    public View getView(int position) {
        return pages.get(position);
    }

    private CountDownTimer createCDT() {
        return new CountDownTimer((hh*3600+mm*60+ss)*1000, 1000) {

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
               // removeCurrentAlarm();
            }

        };
    }

    public void startTimer(){
        cdt = createCDT();
        cdt.start();
    }

    public void cancelTimer() {
        cdt.cancel();
    }

    @Override
    public int getCount() {
        return pages.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private String pad(int num){
        String str = Integer.toString(num);
        if(str.length()==1) str = "0" + str;
        return str;
    }

    private void updateCountdownText() {
        countdown.setText(pad(hh)+":"+pad(mm)+":"+pad(ss));
    }

    @Override
    public int getItemPosition(Object object) {
        View view = (View) object;
        if(pages.contains(view)){
            return pages.indexOf(view);
        } else {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
