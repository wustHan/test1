package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testapplication.Adapter.InviteAdapter;
import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.InvationInfo;
import com.example.testapplication.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class InviteActivity extends AppCompatActivity {

    private ListView iv_invite;
    private InviteAdapter inviteAdapter;
    private LocalBroadcastManager mLBM;
    private InviteAdapter.OnInviteListener mOnInviteListener = new InviteAdapter.OnInviteListener() {
        @Override
        public void onAccept(InvationInfo invationInfo) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(invationInfo.getUser().getHxid());
                        //数据库更新
                        Model.getInstance().getDbManager().getInviteTableDao().updateInvitationStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT,invationInfo.getUser().getHxid());
                        //页面变化
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"接受了邀请",Toast.LENGTH_SHORT).show();

                                refresh();
                            }


                        });
                        //互信页面
                    } catch (HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"接受失败"+e,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onReject(InvationInfo invationInfo) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(invationInfo.getUser().getHxid());

                        Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(invationInfo.getUser().getHxid());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝成功",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝失败"+e,Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }

        //接受邀请
        @Override
        public void onInviteAccept(InvationInfo invationInfo) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().acceptInvitation(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson());
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_INVITE);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"接受邀请",Toast.LENGTH_SHORT).show();

                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"接受邀请失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        //拒绝邀请
        @Override
        public void onInviteReject(InvationInfo invationInfo) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().declineInvitation(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson(),"拒绝邀请");
                        //更新本地数据
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_INVITE);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝邀请",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝邀请失败",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }
        //接受申请
        @Override
        public void onApplicationAccept(InvationInfo invationInfo) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().acceptApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson());
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"接受申请",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"接受申请失败",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }

        //拒绝申请
        @Override
        public void onApplicationReject(InvationInfo invationInfo) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().declineApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson(),"拒绝申请");
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_APPLICATION);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝申请",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝申请失败",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }
    };
    private BroadcastReceiver InviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        initView();
        initData();
    }

    private void initData() {
        inviteAdapter = new InviteAdapter(this, mOnInviteListener);
        iv_invite.setAdapter(inviteAdapter);

        refresh();
        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(InviteChangedReceiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(InviteChangedReceiver,new IntentFilter(Constant.GROUP_INVITE_CHANGED));
    }

    private void refresh() {
        List<InvationInfo> invationInfos = Model.getInstance().getDbManager().getInviteTableDao().getInvitations();
        inviteAdapter.refresh(invationInfos);
    }

    private void initView() {
        iv_invite = (ListView) findViewById(R.id.iv_invite);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(InviteChangedReceiver);
    }
}