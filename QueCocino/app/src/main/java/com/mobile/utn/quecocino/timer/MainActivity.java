package com.mobile.utn.quecocino.timer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobile.utn.quecocino.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    public EditText editText;

    @BindView(R.id.setTimer)
    public Button setTimer;

    @BindView(R.id.openTimer)
    public Button openTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTimer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = editText.getText().toString();
                int timeLength = time.length();
                time = ("000000" + time).substring(timeLength,timeLength+6);
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                intent.putExtra("time", time);
                startActivity(intent);
            }
        });

        openTimer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });

    }
}
