package com.example.testapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.model.bean.PickToDoInfo;
import com.example.testapplication.model.bean.ToDoInfo;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TodoAdapter extends ArrayAdapter<ToDoInfo> {

    private int resourceId;
    public TodoAdapter(@NonNull Context context, int resource, @NonNull List<ToDoInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToDoInfo toDoInfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        CheckBox ad_check = (CheckBox)view.findViewById(R.id.ad_check);
        TextView ad_title = (TextView)view.findViewById(R.id.ad_title);
        TextView ad_content = (TextView)view.findViewById(R.id.ad_content);
        TextView ad_time = (TextView)view.findViewById(R.id.ad_time);
        ImageView ad_impotence = (ImageView)view.findViewById(R.id.impontence_color);

//个性化显示时间
        String end = toDoInfo.getEndTime();

        String ss[] = end.split("&&");
        String s1 = ss[0];//年月日
        String s2 = ss[1];//时分秒

        String S1[] = s1.split("\\.");
        String s11 = S1[0];//年
        String s12 = S1[1];//月
        String s13 = S1[2];//日

        String S2[] = s2.split(":");
        if(s11.equals("2021")){
            ad_time.setText(s12+"月"+s13+"日"+"  "+s2);
        }else {
            ad_time.setText(s11+"年"+s12+"月"+s13+"日"+"  "+s2);
        }

        ad_title.setText(toDoInfo.getToDo_title());
        ad_content.setText(toDoInfo.getToDo_place());

        ad_impotence.setImageResource(toDoInfo.getColor());
        ad_check.setChecked(toDoInfo.getIfDone() != 0);
        return view;
    }
}
