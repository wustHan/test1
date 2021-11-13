package com.example.testapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.model.bean.PickGroupInfo;

import java.util.ArrayList;
import java.util.List;

public class PickGroupAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickGroupInfo> mPicks = new ArrayList<>();
    public PickGroupAdapter(Context context , List<PickGroupInfo> picks){
        mContext = context;
        if(picks!=null&&picks.size()>=0){
            mPicks.clear();
            mPicks.addAll(picks);
        }

    }

    public void refresh(List<PickGroupInfo> mpicks){
        if(mpicks!=null&&mpicks.size()>=0){
            mPicks.clear();
            mPicks.addAll(mpicks);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mPicks == null?0:mPicks.size();
    }

    @Override
    public Object getItem(int position) {
        return mPicks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_pickgroup,null);

            holder.cb = (CheckBox)convertView.findViewById(R.id.pick_group_check);
            holder.tv = (TextView)convertView.findViewById(R.id.pickGroupName);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        PickGroupInfo pickGroupInfo = mPicks.get(position);

        holder.tv.setText(pickGroupInfo.getEmGroup().getGroupName());
        holder.cb.setChecked(pickGroupInfo.isChecked());

        return convertView;
    }

    public List<String> getPickGroupMemberNames() {


        List<String> pickGroup = new ArrayList<>();
        for(PickGroupInfo pickGroupInfo : mPicks){
            pickGroup.addAll(pickGroupInfo.getEmGroup().getMembers());
        }

        return pickGroup;
    }


    private class ViewHolder{
        private CheckBox cb;
        private TextView tv;
    }

}

