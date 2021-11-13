package com.example.testapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.model.bean.TodoModel;

import java.util.List;

import androidx.annotation.NonNull;

public class ModelAdapter extends ArrayAdapter<TodoModel>{
    private int resourceId;
    public ModelAdapter(@NonNull Context context, int resource, @NonNull List<TodoModel> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoModel todoModel = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView modelImage = (ImageView) view.findViewById(R.id.model_picture);
        TextView modelName = (TextView) view.findViewById(R.id.model_name);
//填充数据
        modelImage.setImageResource(R.drawable.basic_model); //根据图片id值加载图片
        modelName.setText(todoModel.getModelName());
        return view;
    }
}
