package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.testapplication.Adapter.ModelAdapter;
import com.example.testapplication.model.bean.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class PickModelActivity extends AppCompatActivity {

    private SearchView search_model;
    private GridView models;
    private String Type;
    private List<TodoModel> list  = new ArrayList<TodoModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_model);

        Intent intent = getIntent();
        Type = intent.getStringExtra("Type");
        initView();
        initData();
        listener();
    }

    private void initData() {
        initGridView();
    }

    private void initGridView() {
        TodoModel basicModel = new TodoModel(1,"基本模板");
        TodoModel simpleModel = new TodoModel(2,"简易模板");
        TodoModel homeworkModel = new TodoModel(3,"作业模板");
        TodoModel Model1 = new TodoModel(4,"疫情打卡");
        TodoModel Model2 = new TodoModel(5,"团建活动");
        TodoModel Model3 = new TodoModel(6,"今日课程");

        list.add(basicModel);
        list.add(simpleModel);
        list.add(homeworkModel);
        list.add(Model1);
        list.add(Model2);
        list.add(Model3);
        ModelAdapter adapter = new ModelAdapter(PickModelActivity.this,R.layout.item_model,list);
        models.setAdapter(adapter);
    }

    private void listener() {

        models.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(PickModelActivity.this, CreateMyToDoActivity.class);
                        intent.putExtra("Type",Type);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(PickModelActivity.this, CreateSimpleToDoActivity.class);
                        intent1.putExtra("Type",Type);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(PickModelActivity.this, HomeWorkActivity.class);
                        intent2.putExtra("Type",Type);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void initView() {
        search_model = (SearchView)findViewById(R.id.search_model);
        models = (GridView)findViewById(R.id.models);
    }
}