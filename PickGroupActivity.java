package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.Adapter.PickGroupAdapter;
import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.GroupInfo;
import com.example.testapplication.model.bean.PickGroupInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class PickGroupActivity extends AppCompatActivity {

    private TextView  group_save;
    private ListView lv_pick_group;
    private List<EMGroup> mGroups;
    private List<PickGroupInfo> pickGroupInfos;
    private PickGroupAdapter pickGroupAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_group);

        initView();
        initData();

        initListener();
    }

    private void initListener() {

        lv_pick_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox)view.findViewById(R.id.pick_group_check);
                checkBox.setChecked(!checkBox.isChecked());

                PickGroupInfo pickGroupInfo = pickGroupInfos.get(position);
                pickGroupInfo.setChecked(checkBox.isChecked());

                pickGroupAdapter.notifyDataSetChanged();
            }
        });

        group_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> names = pickGroupAdapter.getPickGroupMemberNames();

                Intent intent = new Intent();
                intent.putExtra("members",names.toArray(new String[0]));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void initData() {

        pickGroupAdapter = new PickGroupAdapter(this ,pickGroupInfos);
        lv_pick_group.setAdapter(pickGroupAdapter);

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mGroups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    Log.e("groupSize", String.valueOf(mGroups.size()));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pickGroupInfos = new ArrayList<>();
                            if(mGroups!=null&&mGroups.size()>=0){
                                for(EMGroup mGroup : mGroups){
                                    PickGroupInfo pickGroupInfo = new PickGroupInfo(mGroup,false);
                                    pickGroupInfos.add(pickGroupInfo);
                                }
                            }
                            pickGroupAdapter.refresh(pickGroupInfos);

                            Toast.makeText(PickGroupActivity.this, "数据加载成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initView() {
        group_save = (TextView)findViewById(R.id.group_save);
        lv_pick_group = (ListView)findViewById(R.id.lv_pick_group);

    }
}