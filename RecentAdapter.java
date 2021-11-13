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

public class RecentAdapter extends ArrayAdapter<ToDoInfo> {
    private int resourceId;
    public RecentAdapter(@NonNull Context context, int resource, @NonNull List<ToDoInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToDoInfo toDoInfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView title_recent = (TextView)view.findViewById(R.id.title_recent);


        title_recent.setText(toDoInfo.getToDo_title());

        return view;
    }
}
