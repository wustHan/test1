package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class GetClockActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    //获取系统时间
    //小时
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    //分钟
    int minute = calendar.get(Calendar.MINUTE);
    //秒
    int second = calendar.get(Calendar.SECOND);

    private TextView shijian;
    private TimePicker lltimePicker;
    private Button timebutton;
    String s="00:00:00";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_clock);
        initView();
        lltimePicker.setIs24HourView(true);
        lltimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                s=hourOfDay+":"+minute+":00";
                shijian.setText(s);
            }
        });

        timebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                s = shijian.getText().toString();
                bundle.putString("time",s);
                intent.putExtras(bundle);
                setResult(RESULT_OK , intent);
                finish();
            }
        });
    }

    private void initView() {
        shijian = (TextView)findViewById(R.id.shijian);
        lltimePicker = (TimePicker)findViewById(R.id.lltimePicker);
        timebutton = (Button)findViewById(R.id.timebutton);

        shijian.setText(hour+":"+minute+":"+second);
    }
}