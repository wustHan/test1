package com.example.testapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.model.bean.ToDoInfo;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UnProcessedTodoAdapter extends ArrayAdapter<ToDoInfo> {
    private int resourceId;

    public UnProcessedTodoAdapter(@NonNull Context context, int resource, @NonNull List<ToDoInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToDoInfo toDoInfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView sender = (TextView)view.findViewById(R.id.unprocess_sender);
        TextView ad_title = (TextView)view.findViewById(R.id.unprocess_title);
        TextView ad_content = (TextView)view.findViewById(R.id.unprocess_place);
        TextView ad_time = (TextView)view.findViewById(R.id.unprocess_time);

        //个性化显示时间
        String end = toDoInfo.getEndTime();

        String ss[] = end.split("&&");
        String s1 = ss[0];//年月日
        String s2 = ss[1];//时分秒

        String S1[] = s1.split("\\.");
        String s11 = S1[0];//年
        String s12 = S1[1];//月
        String s13 = S1[2];//日

        Calendar calendar = Calendar.getInstance();
        if(s11.equals(calendar.get(Calendar.YEAR))){
            ad_time.setText(s12+"月"+s13+"日"+"  "+s2);
        }else {
            ad_time.setText(s11+"年"+s12+"月"+s13+"日"+"  "+s2);
        }
        ad_title.setText(toDoInfo.getToDo_title());
        ad_content.setText(toDoInfo.getToDo_place());

        sender.setText(toDoInfo.getToDo_sender());
        return view;
    }
}
