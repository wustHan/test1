package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.Adapter.GroupListAdapter;
import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.GroupInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupListActivity extends AppCompatActivity {

    private LinearLayout ll_add_group;
    private ListView group_list;
    private GroupListAdapter groupListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        initView();
        initData();
        listener();
    }

    private void listener() {
        ll_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupListActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }
        });



        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EMGroup myGroup = (EMGroup)group_list.getItemAtPosition(position);
                String pickedGroupId = myGroup.getGroupId();
                Intent intent = new Intent(GroupListActivity.this, GroupDetailActivity.class);
                intent.putExtra("groupId",pickedGroupId);
                startActivity(intent);
            }
        });
    }



    private void initData() {
        groupListAdapter = new GroupListAdapter(this);
        group_list.setAdapter(groupListAdapter);
        getGroupFromServer();
    }

    private void getGroupFromServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EMGroup> mGroups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this,"加载信息成功",Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this,"加载失败"+e,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void refresh(){
        groupListAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }

    private void initView() {

        group_list = (ListView)findViewById(R.id.group_list);

        View headerView = View.inflate(this, R.layout.header_grouplist, null);

        ll_add_group = (LinearLayout) headerView.findViewById(R.id.ll_add_group);

        group_list.addHeaderView(headerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}