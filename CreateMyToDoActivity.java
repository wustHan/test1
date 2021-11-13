package com.example.testapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.ToDoInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.Calendar;

public class CreateMyToDoActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    //月
    int month = calendar.get(Calendar.MONTH)+1;
    //日
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    //获取系统时间
    //小时
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    //分钟
    int minute = calendar.get(Calendar.MINUTE);
    //秒
    int second = calendar.get(Calendar.SECOND);

    private EditText toDo_title;
    private EditText toDo_place;
    private EditText toDo_content;
    private TextView toDo_startDate;
    private TextView toDO_startClock;
    private TextView toDo_endDate;
    private TextView toDo_endClock;
    private Spinner color_spinner;
    private TextView toDo_sender;
    private EditText toDo_receiverGroup;
    private EditText toDO_somethingElse;
    private CheckBox toDo_ifTop;
    private Button bt_toDo_ok;

    private LinearLayout ll_if_top;
    private TextView to_do_impotence;
    private String type;
    private String Message;

    private int colorNum ;
    private int ifCheck = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_my_to_do);
        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
        initView();
        listener();

    }

    private void listener() {
        toDo_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateMyToDoActivity.this, GetDateActivity.class);
                startActivityForResult(intent,100);
            }
        });
        toDo_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateMyToDoActivity.this, GetDateActivity.class);
                startActivityForResult(intent,200);
            }
        });
        toDO_startClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateMyToDoActivity.this, GetClockActivity.class);
                startActivityForResult(intent,300);
            }
        });
        toDo_endClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateMyToDoActivity.this, GetClockActivity.class);
                startActivityForResult(intent,400);
            }
        });

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

        bt_toDo_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDoInfo toDoInfo = new ToDoInfo();
                toDoInfo.setToDo_title(toDo_title.getText().toString());
                if(!TextUtils.isEmpty(toDo_place.getText().toString())){
                    toDoInfo.setToDo_place(toDo_place.getText().toString());
                }else {
                    toDoInfo.setToDo_place("未定");
                }
                if(!TextUtils.isEmpty(toDo_content.getText().toString())){
                    toDoInfo.setToDo_content(toDo_content.getText().toString());
                }else {
                    toDoInfo.setToDo_content("暂无");
                }
                if(!TextUtils.isEmpty(toDo_receiverGroup.getText().toString())){
                    toDoInfo.setToDo_receiverGroup(toDo_receiverGroup.getText().toString());
                }else {
                    toDoInfo.setToDo_receiverGroup("未定");
                }
                if(!TextUtils.isEmpty(toDO_somethingElse.getText().toString())){
                    toDoInfo.setToDO_somethingElse(toDO_somethingElse.getText().toString());
                }else {
                    toDoInfo.setToDO_somethingElse("未定");
                }

                toDoInfo.setStartTime(toDo_startDate.getText().toString()+"&&"+toDO_startClock.getText().toString());
                toDoInfo.setEndTime(toDo_endDate.getText().toString()+"&&"+toDo_endClock.getText().toString());
                toDoInfo.setColor(colorToNum(colorNum));
                toDoInfo.setToDo_sender(toDo_sender.getText().toString());

                toDoInfo.setToDo_ifTop(toDo_ifTop.isChecked()?1:0);
                toDoInfo.setModel_type(1);
                toDoInfo.setIfDone(0);

                String title = toDo_title.getText().toString();
                if(!TextUtils.isEmpty(title)){
                    Message = toDoInfo.toString();

                    if(type.equals("0")){//自建待办
                        toDoInfo.setType(1);//已处理标识
                        Model.getInstance().getTodoTableDao().saveToDo(toDoInfo);
                        finish();
                    }else if(type.equals("1")){//发送待办

                        if(toDo_ifTop.isChecked()){
                            toDoInfo.setType(1);//已处理标识
                            Model.getInstance().getTodoTableDao().saveToDo(toDoInfo);
                        }
                        chosetarget();


                    }
                }else {
                    Toast.makeText(CreateMyToDoActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
//选择接受者
    private void chosetarget() {
        AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(this);
        alertdialogbuilder.setPositiveButton("联系人", click1);
        alertdialogbuilder.setNegativeButton("群组", click2);
        AlertDialog alertdialog1=alertdialogbuilder.create();
        alertdialog1.show();
    }

    private DialogInterface.OnClickListener click2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(CreateMyToDoActivity.this, PickGroupActivity.class);
            startActivityForResult(intent,520);
        }
    };

    private DialogInterface.OnClickListener  click1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(CreateMyToDoActivity.this, PickContactActivity.class);
            startActivityForResult(intent,520);
        }
    };

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


    private void initView() {
        toDo_title = (EditText)findViewById(R.id.toDo_title);
        toDo_place = (EditText)findViewById(R.id.toDo_place);
        toDo_content = (EditText)findViewById(R.id.toDo_content);
        toDo_startDate = (TextView)findViewById(R.id.toDo_startDate);
        toDO_startClock = (TextView)findViewById(R.id.toDO_startClock);
        toDo_endDate = (TextView)findViewById(R.id.toDo_endDate);
        toDo_endClock = (TextView)findViewById(R.id.toDo_endClock);
        color_spinner = (Spinner)findViewById(R.id.color_spinner);
        toDo_sender = (TextView)findViewById(R.id.toDo_sender);
        toDo_receiverGroup= (EditText)findViewById(R.id.toDo_receiverGroup);
        toDO_somethingElse = (EditText)findViewById(R.id.toDO_somethingElse);
        toDo_ifTop = (CheckBox)findViewById(R.id.toDo_ifTop);
        bt_toDo_ok = (Button)findViewById(R.id.bt_toDo_ok);

        to_do_impotence = (TextView)findViewById(R.id.to_do_impotence);
        ll_if_top = (LinearLayout)findViewById(R.id.ll_if_top);

        if(type.equals("0")){//自建待办
            ll_if_top.setVisibility(View.GONE);
            to_do_impotence.setVisibility(View.VISIBLE);
            color_spinner.setVisibility(View.VISIBLE);
            bt_toDo_ok.setText("保存");
        }else if(type.equals("1")){
            //发送待办
            ll_if_top.setVisibility(View.VISIBLE);
            to_do_impotence.setVisibility(View.GONE);
            color_spinner.setVisibility(View.GONE);
            bt_toDo_ok.setText("选择接受者");
        }

        toDo_startDate.setText(year +"."+month+"."+day);
        toDo_endDate.setText(year +"."+month+"."+day);

        toDO_startClock.setText(hour+":"+minute+":"+second);
        toDo_endClock.setText(hour+":"+minute+":"+second);

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String currentUser = EMClient.getInstance().getCurrentUser();
                toDo_sender.setText(currentUser);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(resultCode == RESULT_OK){
                Bundle b = data.getExtras();
                String se = b.getString("date");
                toDo_startDate.setText(se);
            }
        }
        else if(requestCode == 200){
            if(resultCode == RESULT_OK){
                Bundle b = data.getExtras();
                String se = b.getString("date");
                toDo_endDate.setText(se);
            }
        }else if(requestCode == 300){
            if(resultCode == RESULT_OK){
                Bundle b = data.getExtras();
                String se = b.getString("time");
                toDO_startClock.setText(se);
            }
        }else if(requestCode == 400){
            if(resultCode == RESULT_OK){
                Bundle b = data.getExtras();
                String se = b.getString("time");
                toDo_endClock.setText(se);
            }
        }else if(requestCode==520){
            if(resultCode == RESULT_OK){
                Log.e("fasong","回调了");
                String[] members = data.getStringArrayExtra("members");
                for (String member :members){
                    //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                    EMMessage message = EMMessage.createTxtSendMessage(Message, member);

                    EMClient.getInstance().chatManager().sendMessage(message);

                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(CreateMyToDoActivity.this, "消息发送成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(int code, String error) {
                            Toast.makeText(CreateMyToDoActivity.this, "消息发送失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }
                    });
                }
                finish();
            }
        }
    }
}