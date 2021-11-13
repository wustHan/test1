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

public class CreateSimpleToDoActivity extends AppCompatActivity {

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


    private EditText simple_title;
    private EditText simple_place;
    private TextView simple_date;
    private TextView simple_clock;
    private EditText simple_group;
    private Spinner simple_spinner;
    private CheckBox simple_check;
    private Button simple_button;
    private LinearLayout ll_simple_keep;

    private LinearLayout ll_simple_top;

    private String type;
    private int colorNum ;
    private String sender;

    private String Message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_simple_to_do);

        Intent intent = getIntent();
        type = intent.getStringExtra("Type");

        initView();
        initData();
        initListener();
    }

    private void initListener() {
        simple_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSimpleToDoActivity.this, GetDateActivity.class);
                startActivityForResult(intent,138);
            }
        });
        simple_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSimpleToDoActivity.this, GetClockActivity.class);
                startActivityForResult(intent,139);
            }
        });

        simple_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colorNum = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                colorNum = 0;
            }
        });

        simple_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDoInfo toDoInfo = new ToDoInfo();
                toDoInfo.setToDo_title(simple_title.getText().toString());
                toDoInfo.setToDo_place(simple_place.getText().toString());
                toDoInfo.setEndTime(simple_date.getText().toString()+"&&"+simple_clock.getText().toString());
                toDoInfo.setColor(colorToNum(colorNum));
                toDoInfo.setToDo_receiverGroup(simple_group.getText().toString());
                toDoInfo.setToDo_ifTop(simple_check.isChecked()?1:0);

                toDoInfo.setStartTime(simple_date.getText().toString()+"&&"+simple_clock.getText().toString());
                toDoInfo.setToDO_somethingElse("未定");
                toDoInfo.setToDo_content("暂无");
                toDoInfo.setModel_type(2);
                toDoInfo.setIfDone(0);
                toDoInfo.setToDo_sender(sender);

                if(TextUtils.isEmpty(simple_title.getText().toString())||TextUtils.isEmpty(simple_place.getText().toString())||TextUtils.isEmpty(simple_group.getText().toString())){
                    Toast.makeText(CreateSimpleToDoActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    Message = toDoInfo.toString();
                    if(type.equals("0")){//自建待办
                        toDoInfo.setType(1);
                        Model.getInstance().getTodoTableDao().saveToDo(toDoInfo);
                        finish();
                    }else if(type.equals("1")){//发送待办
                        if(simple_check.isChecked()){
                            toDoInfo.setType(1);
                            Model.getInstance().getTodoTableDao().saveToDo(toDoInfo);
                        }
                        chosetarget();
                    }
                }
            }
        });
    }

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
            Intent intent = new Intent(CreateSimpleToDoActivity.this, PickGroupActivity.class);
            startActivityForResult(intent,520);
        }
    };

    private DialogInterface.OnClickListener  click1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(CreateSimpleToDoActivity.this, PickContactActivity.class);
            startActivityForResult(intent,520);
        }
    };
    private void initData() {
        simple_date.setText(year +"."+month+"."+day);
        simple_clock.setText(hour+":"+minute+":"+second);
        if(type.equals("0")){//自建待办
            ll_simple_top.setVisibility(View.VISIBLE);
            simple_button.setText("保存");
            ll_simple_keep.setVisibility(View.GONE);
        }else if(type.equals("1")){//发送待办
            ll_simple_top.setVisibility(View.GONE);
            simple_button.setText("选择接受者");
            ll_simple_keep.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        simple_title = (EditText)findViewById(R.id.simple_title);
        simple_place = (EditText)findViewById(R.id.simple_place);
        simple_date = (TextView)findViewById(R.id.simple_date);
        simple_clock = (TextView)findViewById(R.id.simple_clock);
        simple_group = (EditText)findViewById(R.id.simple_group);
        simple_spinner = (Spinner)findViewById(R.id.simple_spinner);
        simple_check = (CheckBox)findViewById(R.id.simple_check);
        simple_button = (Button)findViewById(R.id.simple_button);
        ll_simple_top = (LinearLayout)findViewById(R.id.ll_simple_top);
        ll_simple_keep = (LinearLayout)findViewById(R.id.ll_simple_keep) ;

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String currentUser = EMClient.getInstance().getCurrentUser();

                sender = currentUser;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 138){
            if(resultCode == RESULT_OK){
                Bundle b = data.getExtras();
                String se = b.getString("date");
                simple_date.setText(se);
            }
        }else if(requestCode == 139){
            if(resultCode == RESULT_OK){
                Bundle b = data.getExtras();
                String se = b.getString("time");
                simple_clock.setText(se);
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreateSimpleToDoActivity.this, "待办发送成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(int code, String error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreateSimpleToDoActivity.this, "待办发送失败", Toast.LENGTH_SHORT).show();
                                }
                            });
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