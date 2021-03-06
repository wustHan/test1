package com.example.testapplication.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testapplication.Adapter.TodoAdapter;
import com.example.testapplication.CreateMyToDoActivity;
import com.example.testapplication.PickContactActivity;
import com.example.testapplication.PickGroupActivity;
import com.example.testapplication.R;
import com.example.testapplication.model.Model;
import com.example.testapplication.model.bean.PickToDoInfo;
import com.example.testapplication.model.bean.ToDoInfo;
import com.hyphenate.chat.EMClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReadFragment extends Fragment {
    private ListView lv_todothings;
    private TodoAdapter todoAdapter;
    private ImageView clear_toDo;
    private List<ToDoInfo> toDoInfos;
    private ToDoInfo detailToDo;
    private ImageView sort_todo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_read, null);
        initView(view);
        initDate();
        initListener();
        return view;
    }

    private String changeTime(String tim){
        String ss[] = tim.split("&&");
        String s1 = ss[0];//?????????
        String s2 = ss[1];//?????????

        String S1[] = s1.split("\\.");
        String s11 = S1[0];//???
        String s12 = S1[1];//???
        if(Integer.parseInt(s12)<10){
            s12 = "0"+s12;
        }
        String s13 = S1[2];//???
        if(Integer.parseInt(s13)<10){
            s13 = "0"+s13;
        }
        String S2[] = s2.split(":");
        String s21 = S2[0];//???
        if(Integer.parseInt(s21)<10){
            s21 = "0"+s21;
        }
        String s22 = S2[1];//???
        if(Integer.parseInt(s22)<10){
            s22 = "0"+s22;
        }
        String s23 = S2[2];//???
        if(Integer.parseInt(s23)<10){
            s23 = "0"+s23;
        }
        return s11+"-"+s12+"-"+s13+" "+s21+":"+s22+":"+s23;
    }
    private int myCompare(String s,String x){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff=0;
        try {
            Date d1 = df.parse(s);
            Date d2 = df.parse(x);
            diff = d1.getTime() - d2.getTime();
        }catch (Exception e){
        }
        if(diff>0){
            return -1;
        }else if(diff==0){
            return 0;
        }else {
            return 1;
        }

    }
    private void initListener() {
        sort_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = { "?????????","?????????"};
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(getActivity());
                listDialog.setTitle("?????????????????????");
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //?????????
                            Collections.sort(toDoInfos, new Comparator<ToDoInfo>() {
                                @Override
                                public int compare(ToDoInfo o1, ToDoInfo o2) {

                                    return myCompare(changeTime(o1.getEndTime()),changeTime(o2.getEndTime()));
                                }
                            });
                        }else {
                            //?????????
                            Collections.sort(toDoInfos, new Comparator<ToDoInfo>() {
                                @Override
                                public int compare(ToDoInfo o1, ToDoInfo o2) {
                                    if(o1.getColor()>o2.getColor()){
                                        return -1;
                                    }else if(o1.getColor()==o2.getColor()){
                                        return 0;
                                    }else{
                                        return 1;
                                    }
                                }
                            });
                        }
                        if(toDoInfos!=null&&toDoInfos.size()>=0){
                            todoAdapter = new TodoAdapter(getActivity(),R.layout.item_todo,toDoInfos);
                            lv_todothings.setAdapter(todoAdapter);
                        }
                    }
                });
                listDialog.show();
            }
        });

        lv_todothings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox ad_check = (CheckBox) view.findViewById(R.id.ad_check);
                ad_check.setChecked(!ad_check.isChecked());

                //????????????
                ToDoInfo toDoInfo = toDoInfos.get(position);
                int checkStatus = ad_check.isChecked()?1:0;
                Model.getInstance().getTodoTableDao().changeChecked(toDoInfo.getToDo_id(),checkStatus);

                initDate();
            }
        });

        lv_todothings.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//????????????????????????
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                detailToDo = toDoInfos.get(position);

                showDetail();

                return true;
            }
        });

        clear_toDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //??????????????????????????????

                AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(getActivity());
                alertdialogbuilder.setMessage("????????????????????????");
                alertdialogbuilder.setPositiveButton("??????", click1);
                alertdialogbuilder.setNegativeButton("??????", click2);
                AlertDialog alertdialog1=alertdialogbuilder.create();
                alertdialog1.show();
            }
        });
    }
    //????????????
    private DialogInterface.OnClickListener click1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
//            String uid = EMClient.getInstance().getCurrentUser();
//            List<ToDoInfo> recentInfos = Model.getInstance().getTodoTableDao().getAllCheckedToDos(uid);
//            Model.getInstance().getRecentTableDao().saveAllToDo(recentInfos);
            Model.getInstance().getTodoTableDao().changeType();
            initDate();
        }
    };
    //??????
    private DialogInterface.OnClickListener  click2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    private void showDetail() {
        AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(getActivity());
        if(detailToDo.getModel_type()==1){
            alertdialogbuilder.setMessage("????????????  "+detailToDo.getToDo_sender()+"\n"+"??????:  "+detailToDo.getToDo_content()+"\n"+"??????????????? "+detailToDo.getStartTime()+"\n"+"????????? " + detailToDo.getToDo_receiverGroup());
        }
        else {
            alertdialogbuilder.setMessage("????????????  "+detailToDo.getToDo_sender()+"\n"+"????????? " + detailToDo.getToDo_receiverGroup());
        }
        AlertDialog alertdialog1=alertdialogbuilder.create();
        alertdialog1.show();
    }

    private void initDate() {
        String uid = EMClient.getInstance().getCurrentUser();
        toDoInfos = Model.getInstance().getTodoTableDao().getAllToDos(uid);//?????????????????????
        if(toDoInfos!=null&&toDoInfos.size()>=0){
            todoAdapter = new TodoAdapter(getActivity(),R.layout.item_todo,toDoInfos);
            lv_todothings.setAdapter(todoAdapter);
        }

    }

    private void initView(View view) {
        lv_todothings = (ListView) view.findViewById(R.id.lv_todothings);
        clear_toDo = (ImageView) view.findViewById(R.id.clear_todo);
        sort_todo = (ImageView) view.findViewById(R.id.sort_todo);
    }

}
