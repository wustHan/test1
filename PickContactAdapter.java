package com.example.testapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.model.bean.PickContactInfo;

import java.util.ArrayList;
import java.util.List;

public class PickContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickContactInfo> mpicks = new ArrayList<>();
    private List<String> mExistMembers = new ArrayList<>();
    public PickContactAdapter(Context context , List<PickContactInfo> picks ,List<String> existMembers){
        mContext = context;
        if(picks!=null && picks.size()>=0){
            mpicks.clear();
            mpicks = picks;
        }
        mExistMembers.clear();
        mExistMembers.addAll(existMembers);
    }
    @Override
    public int getCount() {
        return mpicks == null ? 0 : mpicks.size();
    }

    @Override
    public Object getItem(int position) {
        return mpicks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView ==null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext , R.layout.item_pick ,null);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb_pick);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_pick_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //获取当前item数据
        PickContactInfo pickContactInfo = mpicks.get(position);
        //显示数据
        holder.tv_name.setText(pickContactInfo.getUser().getName());
        holder.cb.setChecked(pickContactInfo.isChecked());

        if(mExistMembers.contains(pickContactInfo.getUser().getHxid())){
            holder.cb.setChecked(true);
            pickContactInfo.setChecked(true);
        }
        return convertView;
    }

    public List<String> getPickContacts() {
        List<String> picks = new ArrayList<>();
        for (PickContactInfo pick : mpicks){
            if(pick.isChecked()){
                picks.add(pick.getUser().getName());
            }
        }
        return picks;
    }

    private class ViewHolder{
        private CheckBox cb;
        private TextView tv_name;
    }
}
