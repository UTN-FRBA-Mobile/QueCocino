package com.mobile.utn.quecocino.timer;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.utn.quecocino.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Timer extends AppCompatActivity {

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


    @BindView(R.id.timerCount)
    public TextView timerCount;
    private String hhmmss;

    @BindView(R.id.timerDeleteBtn)
    public ImageView deleteBtn;

    @BindView(R.id.timerStart)
    public Button startBtn;
    private CountDownTimer cdt;
    private String[] strArr;
    private boolean timerRunning = false;
    @BindView(R.id.timerReset)
    public Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);

        deleteBtn.getDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        hhmmss = "000000";
        updateTimerCount();

        List<Button> keyboard = Arrays.asList(btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9);
        for (Button keyBtn : keyboard) {

            keyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hhmmss.charAt(0)=='0' && !timerRunning) {
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
                if (!timerRunning) {
                    hhmmss = "0" + hhmmss.substring(0,5);
                    updateTimerCount();
                }
            }

        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerRunning) {
                    startBtn.setText("Stop");
                    strArr = splitStringEvery(hhmmss,2);
                    int hh = Integer.parseInt(strArr[0]);
                    int mm = Integer.parseInt(strArr[1]);
                    int ss = Integer.parseInt(strArr[2]);

                    cdt = new CountDownTimer((hh*3600+mm*60+ss-1)*1000, 1000) {

                        private int hh = Integer.parseInt(strArr[0]);
                        private int mm = Integer.parseInt(strArr[1]);
                        private int ss = Integer.parseInt(strArr[2]);

                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (ss > 0) {
                                ss--;
                                strArr[2] = Integer.toString(ss);
                                if (ss < 10) strArr[2]= "0" + strArr[2];
                                hhmmss = TextUtils.join("", strArr);
                                updateTimerCount();
                            } else if (mm > 0) {
                                mm--;
                                strArr[1] = Integer.toString(mm);
                                if (mm < 10) strArr[1]= "0" + strArr[1];
                                ss = 59;
                                strArr[2] = "59";
                                hhmmss = TextUtils.join("", strArr);
                                updateTimerCount();
                            } else if (hh > 0) {
                                hh--;
                                strArr[0] = Integer.toString(hh);
                                if (hh < 10) strArr[0]= "0" + strArr[0];
                                mm = 59;
                                strArr[1] = "59";
                                ss = 59;
                                strArr[2] = "59";
                                hhmmss = TextUtils.join("", strArr);
                                updateTimerCount();
                            }
                        }

                        @Override
                        public void onFinish() {
                            timerRunning = false;
                            startBtn.setText("Start");
                            hhmmss = "000000";
                            updateTimerCount();
                        }
                    };
                    timerRunning = true;
                    cdt.start();
                } else {
                    cdt.cancel();
                    timerRunning = false;
                    startBtn.setText("Start");
                }
            }

        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerRunning) {
                    cdt.cancel();
                    timerRunning = false;
                    startBtn.setText("Start");
                }
                hhmmss = "000000";
                updateTimerCount();
            }

        });

    }

    private void updateTimerCount() {
        timerCount.setText(hhmmss.replaceAll("(..)(..)(..)", "$1h $2m $3s"));
    }

    public String[] splitStringEvery(String s, int interval) {
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
}
