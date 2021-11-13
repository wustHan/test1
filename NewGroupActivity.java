package com.example.testapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testapplication.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

public class NewGroupActivity extends AppCompatActivity {

    private EditText new_group_name;
    private EditText new_group_desc;
    private CheckBox group_whether_public;
    private CheckBox group_if_invite;
    private Button group_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        initView();
        initListener();
    }

    private void initListener() {
        group_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGroupActivity.this, PickContactActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            createGroup(data.getStringArrayExtra("members"));
        }
    }

    private void createGroup(String[] members) {
        String groupName = new_group_name.getText().toString();
        String groupDesc = new_group_desc.getText().toString();

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //参数一：群名称 参数二：群描述 参数三：群成员 参数四：原因 参数五：参数设置

                EMGroupOptions options = new EMGroupOptions();
                options.maxUsers=200;
                EMGroupManager.EMGroupStyle groupStyle = null;
                if(group_whether_public.isChecked()){
                    if(group_if_invite.isChecked()){
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    }else{
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                }else{
                    if(group_if_invite.isChecked()){
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    }else{
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }
                options.style = groupStyle;
                try {
                    EMClient.getInstance().groupManager().createGroup(groupName,groupDesc,members,"申请加入群",options);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this,"创建群成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this,"创建群失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private void initView() {
        new_group_name = (EditText)findViewById(R.id.new_group_name);
        new_group_desc = (EditText)findViewById(R.id.new_group_desc);
        group_whether_public = (CheckBox)findViewById(R.id.group_whether_public);
        group_if_invite = (CheckBox)findViewById(R.id.group_if_invite);
        group_create = (Button)findViewById(R.id.group_create);
    }
}