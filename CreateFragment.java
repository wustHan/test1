package com.example.testapplication.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.testapplication.Adapter.TodoAdapter;
import com.example.testapplication.Adapter.UnProcessedTodoAdapter;
import com.example.testapplication.CreateMyToDoActivity;
import com.example.testapplication.PickModelActivity;
import com.example.testapplication.ProcessActivity;
import com.example.testapplication.R;
import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.ToDoInfo;
import com.example.testapplication.utils.Constant;
import com.example.testapplication.utils.SpUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class CreateFragment extends Fragment {
    private LinearLayout ll_send_todo;
    private LinearLayout ll_create_my_todo;
    private ListView edit_lv_totodo;
    private UnProcessedTodoAdapter unprocessedtodoAdapter;
    private LocalBroadcastManager mLBM;
    private List<ToDoInfo> allUnprocessedToDos;
    private ImageView unprocessed_red;

    private BroadcastReceiver ChatMessageChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点
            unprocessed_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_MESSAGE,true);

            initdata();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_create, null);

        initView(view);
        initdata();
        listener();
        return view;
    }

    private void initdata() {

        String uid = EMClient.getInstance().getCurrentUser();
        allUnprocessedToDos = Model.getInstance().getTodoTableDao().getAllUnprocessedToDos(uid);
        if(allUnprocessedToDos!=null&&allUnprocessedToDos.size()>=0){
            unprocessedtodoAdapter = new UnProcessedTodoAdapter(getActivity(),R.layout.item_unprocesstodo,allUnprocessedToDos);
            edit_lv_totodo.setAdapter(unprocessedtodoAdapter);
        }
    }

    private void listener() {
        ll_create_my_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PickModelActivity.class);
                intent.putExtra("Type","0");
                startActivity(intent);
            }
        });

        ll_send_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PickModelActivity.class);
                intent.putExtra("Type","1");
                startActivity(intent);
            }
        });

        edit_lv_totodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //红点处理
                unprocessed_red.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_MESSAGE,false);

                ToDoInfo toDoInfo = allUnprocessedToDos.get(position);
                if(toDoInfo.getModel_type()==1){
                    Intent intent = new Intent(getActivity(), ProcessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("toDoInfo",toDoInfo);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,123);
                }else if(toDoInfo.getModel_type() == 2){
                    Intent intent = new Intent(getActivity(), ProcessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("toDoInfo",toDoInfo);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,123);
                }
            }
        });
    }

    private void initView(View view) {

        ll_send_todo = (LinearLayout)view.findViewById(R.id.ll_send_todo);
        ll_create_my_todo = (LinearLayout)view.findViewById(R.id.ll_create_my_todo);
        edit_lv_totodo = (ListView)view.findViewById(R.id.edit_lv_totodo);
        unprocessed_red = (ImageView)view.findViewById(R.id.unprocessed_red);


        boolean isNewMessage = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_MESSAGE, false);

        unprocessed_red.setVisibility(isNewMessage?View.VISIBLE:View.GONE);

        mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.registerReceiver(ChatMessageChangedReceiver,new IntentFilter(Constant.RECEIVING_MESSAGE));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(ChatMessageChangedReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123){
            initdata();
        }
    }
}
