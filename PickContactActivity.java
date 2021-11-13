package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testapplication.Adapter.PickContactAdapter;
import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.PickContactInfo;
import com.example.testapplication.model.bean.UserInfo;
import com.example.testapplication.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

public class PickContactActivity extends AppCompatActivity {

    private TextView tv_pick_save;
    private ListView lv_pick_contact;
    private List<PickContactInfo> picks;
    private PickContactAdapter pickContactAdapter;
    private List<String> mExistMembers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);
        //获取传递过来的数据
        getData();
        initView();
        initData();
        initListener();
    }

    private void getData() {
        String groupId = getIntent().getStringExtra(Constant.GROUP_ID);

        if(groupId!=null){
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
            //获取群众已经存在的所有的群成员
            mExistMembers = group.getMembers();
        }
        if(mExistMembers == null){
            mExistMembers = new ArrayList<>();
        }
    }

    private void initListener() {
        lv_pick_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb_pick = (CheckBox) view.findViewById(R.id.cb_pick);
                cb_pick.setChecked(!cb_pick.isChecked());
                PickContactInfo pickContactInfo = picks.get(position);
                pickContactInfo.setChecked(cb_pick.isChecked());

                pickContactAdapter.notifyDataSetChanged();
            }
        });

        tv_pick_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取选择的联系人
                List<String> names = pickContactAdapter.getPickContacts();
                //给启动页面返回数据
                Intent intent = new Intent();
                intent.putExtra("members",names.toArray(new String[0]));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void initData() {
        //从本地数据库中获取所有联系人
        List<UserInfo> contacts = Model.getInstance().getDbManager().getContactTableDao().getContacts();

        picks = new ArrayList<>();

        if(contacts != null && contacts.size()>=0){
            for (UserInfo contact :contacts){
                PickContactInfo pickContactInfo = new PickContactInfo(contact, false);
                picks.add(pickContactInfo);
            }
        }


        pickContactAdapter = new PickContactAdapter(this,picks , mExistMembers);

        lv_pick_contact.setAdapter(pickContactAdapter);
    }

    private void initView() {
        tv_pick_save = (TextView)findViewById(R.id.tv_pick_save);
        lv_pick_contact = (ListView)findViewById(R.id.lv_pick_contact);
    }
}