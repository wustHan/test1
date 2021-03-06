package com.example.testapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.Adapter.GroupDetailAdapter;
import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.UserInfo;
import com.example.testapplication.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends AppCompatActivity {

    private Button bt_groupDetail_quit;
    private TextView groupDetail_desc;
    private GridView groupDetail_members;
    private TextView groupDetail_name;
    private EMGroup mGroup;
    private  List<UserInfo> mUsers;
    private GroupDetailAdapter groupDetailAdapter;
    private GroupDetailAdapter.OnGroupDetailListener mOnGroupDetailListener = new GroupDetailAdapter.OnGroupDetailListener() {
        @Override
        public void onAddMembers() {
            Intent intent = new Intent(GroupDetailActivity.this, PickContactActivity.class);
            intent.putExtra(Constant.GROUP_ID,mGroup.getGroupId());
            startActivityForResult(intent,2);
        }

        @Override
        public void onDeleteMember(UserInfo user) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().removeUserFromGroup(mGroup.getGroupId(),user.getHxid());
                        getMembersFromHxServer();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "????????????"+e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            //????????????????????????????????????
            String[] members = data.getStringArrayExtra("members");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //????????????
                        EMClient.getInstance().groupManager().addUsersToGroup(mGroup.getGroupId(),members);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        initView();
        getData();
        initData();

        initListener();
    }

    private void initListener() {
        groupDetail_members.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(groupDetailAdapter.ismIsDeleteModel()){
                            //?????????????????????
                            groupDetailAdapter.setmIsDeleteModel(false);
                            //????????????
                            groupDetailAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        //?????????button??????
        initButtonDisplay();
        groupDetail_desc.setText(mGroup.getDescription());
        groupDetail_name.setText(mGroup.getGroupName());
        initGridView();

        //??????????????????????????????????????????
        getMembersFromHxServer();
    }

    private void getMembersFromHxServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroup emGroup = EMClient.getInstance().groupManager().getGroupFromServer(mGroup.getGroupId());

                    List<String> members = emGroup.getMembers();

                    if(members!=null&& members.size()>=0){

                        mUsers = new ArrayList<UserInfo>();
                        for (String member:members){
                            UserInfo userInfo = new UserInfo(member);
                            mUsers.add(userInfo);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groupDetailAdapter.refresh(mUsers);
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupDetailActivity.this, "?????????????????????"+e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initGridView() {
        //?????????????????????????????????????????????
        boolean isCanModify = EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner())||mGroup.isPublic();
        groupDetailAdapter = new GroupDetailAdapter(this, isCanModify , mOnGroupDetailListener);
        groupDetail_members.setAdapter(groupDetailAdapter);
    }

    private void initButtonDisplay() {
        if(EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner())){
            //??????
            bt_groupDetail_quit.setText("?????????");
            bt_groupDetail_quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().destroyGroup(mGroup.getGroupId());

                                //?????????????????????
                                exitGroupBroadCast();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this,"???????????????",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this,"???????????????",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            }
                        }
                    });
                }
            });
        }else {
            //??????
            bt_groupDetail_quit.setText("??????");
            bt_groupDetail_quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().leaveGroup(mGroup.getGroupId());
                                //??????????????????
                                exitGroupBroadCast();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }
//??????
    private void exitGroupBroadCast() {
        LocalBroadcastManager mLBM = LocalBroadcastManager.getInstance(GroupDetailActivity.this);
        Intent intent = new Intent(Constant.EXIT_GROUP);

        intent.putExtra(Constant.GROUP_ID,mGroup.getGroupId());

        mLBM.sendBroadcast(intent);
    }

    private void getData() {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");
        if(groupId == null){
            return;
        }
        else {
            mGroup = EMClient.getInstance().groupManager().getGroup(groupId);
        }
    }

    private void initView() {
        bt_groupDetail_quit = (Button)findViewById(R.id.bt_quit);
        groupDetail_desc = (TextView)findViewById(R.id.groupdetail_desc);
        groupDetail_members = (GridView)findViewById(R.id.groupdetail_members);
        groupDetail_name = (TextView)findViewById(R.id.groupdetail_name);
    }
}