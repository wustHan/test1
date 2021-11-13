package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.testapplication.Fragment.ContactListFragment;
import com.example.testapplication.Fragment.CreateFragment;
import com.example.testapplication.Fragment.MyFragment;
import com.example.testapplication.Fragment.ReadFragment;

public class MainActivity extends FragmentActivity {
    private RadioGroup rg_main;
    private MyFragment myFragment;
    private ContactListFragment contactListFragment;
    private CreateFragment createFragment;
    private ReadFragment readFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;
                switch (checkedId){
                    case R.id.rb_read:
                        fragment = readFragment;
                        break;
                    case R.id.rb_create:
                        fragment =createFragment;
                        break;
                    case R.id.rb_people:
                        fragment = contactListFragment;
                        break;
                    case R.id.rb_my:
                        fragment = myFragment;
                        break;
                }

                switchFragment(fragment);

            }
        });
        rg_main.check(R.id.rb_read);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }

    private void initData() {
        readFragment = new ReadFragment();
        createFragment = new CreateFragment();
        contactListFragment = new ContactListFragment();
        myFragment = new MyFragment();
    }

    private void initView() {
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

    }


}