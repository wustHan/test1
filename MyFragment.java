package com.example.testapplication.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.Adapter.RecentAdapter;
import com.example.testapplication.Adapter.TodoAdapter;
import com.example.testapplication.CreateMyToDoActivity;
import com.example.testapplication.LoginActivity;
import com.example.testapplication.PickContactActivity;
import com.example.testapplication.PickGroupActivity;
import com.example.testapplication.R;
import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.ToDoInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyFragment extends Fragment {
    private Button bt_my_out;
    private Button about_my;
    private TextView clear_my;
    private ListView recent_my;
    private List<ToDoInfo> toDoInfos;
    private ToDoInfo detailToDo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_my, null);
        initView(view);
        initMyData();
        initListener();
        return view;
    }

    private void initMyData() {
        String uid = EMClient.getInstance().getCurrentUser();
        toDoInfos = Model.getInstance().getTodoTableDao().getAllRecent(uid);
        if(toDoInfos!=null&&toDoInfos.size()>=0){
            RecentAdapter adapter = new RecentAdapter(getActivity(),R.layout.item_recent,toDoInfos);
            recent_my.setAdapter(adapter);
        }

    }

    private void initListener() {
        //清空最近待办
        clear_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getTodoTableDao().clearAllRecent();
                initMyData();
            }
        });

        recent_my.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//长按显示具体内容
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                detailToDo = toDoInfos.get(position);

                showDetail();

                return true;
            }
        });
    }

    //显示待办详情
    private void showDetail() {
        AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(getActivity());
        if(detailToDo.getModel_type()==1){
            alertdialogbuilder.setMessage("发送者：  "+detailToDo.getToDo_sender()+"\n"+"正文:  "+detailToDo.getToDo_content()+"\n"+"开始时间： "+detailToDo.getStartTime()+"\n"+"群体： " + detailToDo.getToDo_receiverGroup());
        }
        else {
            alertdialogbuilder.setMessage("发送者：  "+detailToDo.getToDo_sender()+"\n"+"群体： " + detailToDo.getToDo_receiverGroup());
        }
        alertdialogbuilder.setPositiveButton("恢复", click1);
        alertdialogbuilder.setNegativeButton("取消", click2);
        AlertDialog alertdialog1=alertdialogbuilder.create();
        alertdialog1.show();
    }
    private DialogInterface.OnClickListener click2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    //将最近删除转换成已处理
    private DialogInterface.OnClickListener  click1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
           Model.getInstance().getTodoTableDao().changeTypeToProcessed(detailToDo.getToDo_id());
        }
    };
    private void initView(View view) {
        bt_my_out = (Button)view.findViewById(R.id.bt_my_out);
        about_my = (Button)view.findViewById(R.id.about_my);
        clear_my = (TextView)view.findViewById(R.id.clear_my);
        recent_my = (ListView)view.findViewById(R.id.recent_my);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        bt_my_out.setText("退出当前账户("+ EMClient.getInstance().getCurrentUser()+")");

        bt_my_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {

                                Model.getInstance().getDbManager().close();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);

                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(int code, String error) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"退出失败"+error,Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }
                        });
                    }
                });
            }
        });
    }
}
