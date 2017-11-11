package com.mobile.utn.quecocino.timer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.menu.NavigationMenu;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.NOTIFICATION_SERVICE;

public class TimerEditFragment extends Fragment{

    @BindView(R.id.timerTagEdit)
    public EditText tagEdit;

    @BindView(R.id.timerInput)
    public TextView timerInput;

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

    public String hhmmss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer_edit, container, false);
        ButterKnife.bind(this,view);

        hhmmss="000000";
        Bundle args = getArguments();
        if(args!=null){
            hhmmss = (args.containsKey("hhmmss")) ? args.getString("hhmmss") : "000000";
            tagEdit.setText((args.containsKey("tag")) ? args.getString("tag") : "");
        }
        updateTimerInput();

        deleteBtn.getDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        keyboard = Arrays.asList(btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9);
        for (Button keyBtn : keyboard) {
            keyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (hhmmss.charAt(0)=='0') {
                    String num = ((Button) view).getText().toString();
                    hhmmss = hhmmss.substring(1) + num;
                    updateTimerInput();
                }
                }

            });
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hhmmss = "0" + hhmmss.substring(0,5);
                updateTimerInput();
            }

        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
                FragmentManager fragmentManager = getFragmentManager();

                Bundle args = TimerEditFragment.this.getArguments();
                Boolean isAddTimer = args!=null ? args.getBoolean("isAddTimer") : false;
                if(isAddTimer){
                    fragmentManager.popBackStackImmediate();
                } else {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.navigation_container, new TimerCountdownFragment());
                    fragmentTransaction.commit();
                }
            }
        });

        NavigationMenu activity = (NavigationMenu) getActivity();
        List<Integer> itemsId = Arrays.asList(R.id.navigation_action_buscarRecetas,
                                                R.id.navigation_action_favoritos,
                                                    R.id.navigation_action_timers);
        activity.showMenuItems(itemsId);

        return view;
    }

    private void setAlarm(){
        Long millis = System.currentTimeMillis()+getTimeInMillis();
        String tag = tagEdit.getText().toString();
        Intent i = new Intent(this.getActivity(), TimerAlarmReceiver.class);
        int alarmId = AlarmUtils.addAlarm(this.getActivity(), i, millis, tag);
    }

    private void updateTimerInput() {
        if(hhmmss.equals("000000")){
            startBtn.setVisibility(View.INVISIBLE);
        } else {
            startBtn.setVisibility(View.VISIBLE);
        }
        timerInput.setText(hhmmss.replaceAll("(..)(..)(..)", "$1h $2m $3s"));
    }

    private int getTimeInMillis() {
        int hh = Integer.parseInt(hhmmss.substring(0,2));
        int mm = Integer.parseInt(hhmmss.substring(2,4));
        int ss = Integer.parseInt(hhmmss.substring(4,6));
        return (ss + mm * 60 + hh * 3600) * 1000;
    }

}
