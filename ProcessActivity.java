package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.ToDoInfo;

public class ProcessActivity extends AppCompatActivity {

    private TextView process_sender;
    private TextView process_title;
    private TextView process_place;
    private TextView process_content;
    private TextView process_starttime;
    private TextView process_endtime;
    private TextView process_receiver_group;
    private TextView process_somethingelse;
    private Spinner color_spinner;
    private TextView process_delete;
    private CheckBox toDo_ifTop;
    private Button bt_process_save;

    private LinearLayout ll_process_content;
    private LinearLayout ll_process_starttime;
    private TextView process_wishtime;
    private LinearLayout ll_process_fujian;

    private ToDoInfo toDoInfo;
    private int colorNum;

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        initView();
        initData();
        listener();
    }

    private void listener() {

        color_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colorNum = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                colorNum = 0;
            }
        });

        //删除未处理
        process_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getTodoTableDao().deleteUnprocessedToDo(toDoInfo.getToDo_id());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        //将未处理修改为已处理
        bt_process_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDoInfo.setToDo_ifTop(toDo_ifTop.isChecked()?1:0);
                toDoInfo.setIfDone(0);
                toDoInfo.setModel_type(toDoInfo.getModel_type());
                toDoInfo.setColor(colorToNum(colorNum));
                toDoInfo.setType(1);//设置为已处理待办
                Model.getInstance().getTodoTableDao().changeTypeToProcessed(toDoInfo.getToDo_id());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    private int colorToNum(int colorNum) {
        if(colorNum == 0){
            return R.drawable.red;
        }else if(colorNum == 1){
            return R.drawable.orange;
        }else if(colorNum == 2){
            return R.drawable.yellow;
        }else if(colorNum == 3){
            return R.drawable.green;
        }else if(colorNum == 4){
            return R.drawable.blue;
        }else if(colorNum == 5){
            return R.drawable.indigo;
        }else if(colorNum == 6){
            return R.drawable.violet;
        }
        return R.color.black;
    }

    private void initData() {

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        toDoInfo = (ToDoInfo)bundle.getSerializable("toDoInfo");
        process_sender.setText(toDoInfo.getToDo_sender());
        process_title.setText(toDoInfo.getToDo_title());
        process_place.setText(toDoInfo.getToDo_place());
        process_content.setText(toDoInfo.getToDo_content());
        process_starttime.setText(toDoInfo.getStartTime());
        process_endtime.setText(toDoInfo.getEndTime());
        process_receiver_group.setText(toDoInfo.getToDo_receiverGroup());
        process_somethingelse.setText(toDoInfo.getToDO_somethingElse());
        if(toDoInfo.getModel_type() == 2){
            ll_process_content.setVisibility(View.GONE);
            ll_process_starttime.setVisibility(View.GONE);
            process_wishtime.setText("时间");
            ll_process_fujian.setVisibility(View.GONE);
        }
    }

    private void initView() {
        process_sender = (TextView)findViewById(R.id.process_sender);
        process_title = (TextView)findViewById(R.id.process_title);
        process_place = (TextView)findViewById(R.id.process_place);
        process_content=(TextView)findViewById(R.id.process_content);
        process_starttime = (TextView)findViewById(R.id.process_starttime);
        process_endtime = (TextView)findViewById(R.id.process_endtime);
        process_receiver_group = (TextView)findViewById(R.id.process_receiver_group);
        process_somethingelse = (TextView)findViewById(R.id.process_somethingelse);
        process_delete = (TextView)findViewById(R.id.process_delete);
        color_spinner = (Spinner)findViewById(R.id.color_spinner1);
        toDo_ifTop = (CheckBox)findViewById(R.id.toDo_ifTop1);
        bt_process_save = (Button)findViewById(R.id.bt_process_save);

        ll_process_content = (LinearLayout)findViewById(R.id.ll_process_content);
        ll_process_starttime = (LinearLayout)findViewById(R.id.ll_process_starttime);
        process_wishtime = (TextView)findViewById(R.id.process_wishtime);
        ll_process_fujian = (LinearLayout)findViewById(R.id.ll_process_fujian);
    }
}