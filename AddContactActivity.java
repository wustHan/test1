package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AddContactActivity extends AppCompatActivity {

    private ImageView im_search;
    private EditText tv_add_find;
    private Button bt_add;
    private TextView tv_add_name;
    private LinearLayout outcome;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        init();
        listener();
    }

    private void listener() {
        im_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    private void find() {
        String name = tv_add_find.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(AddContactActivity.this,"输入的用户名不能为空",Toast.LENGTH_SHORT).show();
        }
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //默认存在该用户
                userInfo = new UserInfo(name);
                //更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        outcome.setVisibility(View.VISIBLE);
                        tv_add_name.setText(userInfo.getName());
                    }
                });
            }
        });
    }

    private void add() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(userInfo.getName(),"添加好友");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this,"发送好友申请成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this,"发送好友申请失败"+e,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void init() {
        im_search = (ImageView)findViewById(R.id.im_search);
        tv_add_find = (EditText) findViewById(R.id.tv_add_find);
        tv_add_name = (TextView)findViewById(R.id.tv_add_name);
        bt_add = (Button)findViewById(R.id.bt_add);
        outcome = (LinearLayout)findViewById(R.id.outcome);
    }
}