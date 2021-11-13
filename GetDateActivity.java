package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class GetDateActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    int year1 = calendar.get(Calendar.YEAR);
    //月
    int month1 = calendar.get(Calendar.MONTH)+1;
    //日
    int day1 = calendar.get(Calendar.DAY_OF_MONTH);
    //获取系统时间

    private DatePicker lldatePicker;
    private Button dateButton;
    private TextView riqi;
    int year,month,day;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_date);
        initView();

        lldatePicker.init(year1, month1, day1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                GetDateActivity.this.year = year;
                GetDateActivity.this.month = monthOfYear;
                GetDateActivity.this.day = dayOfMonth;
                riqi.setText(GetDateActivity.this.year+"."+GetDateActivity.this.month+"."+GetDateActivity.this.day);
            }
        });


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                s = riqi.getText().toString();
                bundle.putString("date",s);
                intent.putExtras(bundle);
                setResult(RESULT_OK , intent);
                finish();
            }
        });

    }

    private void initView() {
        lldatePicker = (DatePicker)findViewById(R.id.lldatePicker);
        dateButton = (Button)findViewById(R.id.datebutton);
        riqi = (TextView)findViewById(R.id.riqi);

        riqi.setText(year1+"."+month1+"."+day1);
    }
}