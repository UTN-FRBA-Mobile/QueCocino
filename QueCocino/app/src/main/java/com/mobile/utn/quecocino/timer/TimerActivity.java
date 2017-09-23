package com.mobile.utn.quecocino.timer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.utn.quecocino.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.mobile.utn.quecocino.timer.TimerService.TimerServiceBinder;

public class TimerActivity extends AppCompatActivity implements TimerObserver{

    @BindView(R.id.timerCount)
    public TextView timerCount;

    @BindView(R.id.timerBtn0)
    public Button btn0;
    @BindView(R.id.timerBtn1)
    public Button btn1;
    @BindView(R.id.timerBtn2)
    public Button btn2;
    @BindView(R.id.timerBtn3)
    public Button btn3;
    @BindView(R.id.timerBtn4)
    public Button btn4;
    @BindView(R.id.timerBtn5)
    public Button btn5;
    @BindView(R.id.timerBtn6)
    public Button btn6;
    @BindView(R.id.timerBtn7)
    public Button btn7;
    @BindView(R.id.timerBtn8)
    public Button btn8;
    @BindView(R.id.timerBtn9)
    public Button btn9;
    public List<Button> keyboard;

    @BindView(R.id.timerDeleteLayout)
    public LinearLayout deleteLayout;
    @BindView(R.id.timerDeleteBtn)
    public ImageView deleteBtn;
    @BindView(R.id.timerStart)
    public Button startBtn;
    @BindView(R.id.timerReset)
    public Button resetBtn;

    public String hhmmss;

    private ServiceConnection timerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerServiceBinder timerServiceBinder = (TimerServiceBinder) service;
            timerService = timerServiceBinder.getService();
            timerService.stopForeground(true);
            timerService.registrateObserver(TimerActivity.this);

            Intent intent = TimerActivity.this.getIntent();
            String time = intent.getStringExtra("time");
            if(timerService.isTimerRunning()){
                if(time!=null){
                    Toast.makeText(TimerActivity.this, "El timer está activo, solo uno a la vez", Toast.LENGTH_LONG).show();
                }
                onTimerTick();
                boolean timerFinished = intent.getBooleanExtra("timerFinished",false);
                if(timerFinished){
                    onTimerFinish();
                } else {
                    setTimerRunningUI();
                }
            } else {
                if(time!=null){
                    hhmmss = time;
                } else {
                    hhmmss = "000000";
                }
                updateTimerCount();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private TimerService timerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);

        deleteBtn.getDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        Intent i = new Intent(this, TimerService.class);
        startService(i);
        bindService(i, timerServiceConnection, BIND_AUTO_CREATE);

        keyboard = Arrays.asList(btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9);
        for (Button keyBtn : keyboard) {

            keyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hhmmss.charAt(0)=='0') {
                        String num = ((Button) view).getText().toString();
                        hhmmss = hhmmss.substring(1) + num;
                        updateTimerCount();
                    }
                }

            });

        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hhmmss = "0" + hhmmss.substring(0,5);
                updateTimerCount();
            }

        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerService.isTimerRunning() && hhmmss!="000000") {
                    runTimer();
                } else if (timerService.isTimerRunning()) {
                    stopTimer();
                }
            }

        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                hhmmss = "000000";
                updateTimerCount();
            }

        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        timerService.stopForeground(true);
        timerService.registrateObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerService.unregistrateObserver();
        timerService.startAsForeground();
    }

    private void setTimerRunningUI(){
        startBtn.setText("Stop");
        resetBtn.setVisibility(View.VISIBLE);
        deleteLayout.setVisibility(View.GONE);
        setKeyboardVisibility(View.GONE);
        timerCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 54);
        timerCount.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void setTimerStoppedUI(){
        startBtn.setText("Start");
        resetBtn.setVisibility(View.GONE);
        deleteLayout.setVisibility(View.VISIBLE);
        setKeyboardVisibility(View.VISIBLE);
        timerCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        timerCount.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
    }

    private void runTimer(){
        setTimerRunningUI();
        String[] strArr = splitStringEvery(hhmmss,2);
        int hh = Integer.parseInt(strArr[0]);
        int mm = Integer.parseInt(strArr[1]);
        int ss = Integer.parseInt(strArr[2]);
        timerService.runTimer(hh,mm,ss);
    }

    private void stopTimer(){
        timerService.stopTimer();
        setTimerStoppedUI();
    }

    private void updateTimerCount() {
        timerCount.setText(hhmmss.replaceAll("(..)(..)(..)", "$1h $2m $3s"));
    }

    private String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = s.substring(j);

        return result;
    }

    private void setKeyboardVisibility(int visivility) {
        for (Button keyBtn : keyboard) {
            keyBtn.setVisibility(visivility);
        }
    }

    @Override
    public void onTimerTick() {
        hhmmss = timerService.getTime();
        updateTimerCount();
    }

    @Override
    public void onTimerFinish() {
        setTimerStoppedUI();
    }


}
