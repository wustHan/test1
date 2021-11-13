package com.example.testapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailAdapter extends BaseAdapter {
    private Context mContext;
    private boolean mIsCanModify;
    private List<UserInfo> mUsers = new ArrayList<>();
    private boolean mIsDeleteModel;//删除模式 true表示可删除
    private OnGroupDetailListener mOnGroupDetailListener;
    public GroupDetailAdapter(Context context , boolean isCanModify ,OnGroupDetailListener onGroupDetailListener){
        mContext = context;
        mIsCanModify = isCanModify;
        mOnGroupDetailListener = onGroupDetailListener;
    }
    //获取当前的删除模式
    public boolean ismIsDeleteModel() {
        return mIsDeleteModel;
    }
    //设置当前的删除模式
    public void setmIsDeleteModel(boolean mIsDeleteModel) {
        this.mIsDeleteModel = mIsDeleteModel;
    }

    public void refresh(List<UserInfo> users){
        if(users!=null&& users.size() >= 0){
            mUsers.clear();

            initUsers();

            mUsers.addAll(0,users);
        }
        notifyDataSetChanged();
    }

    private void initUsers() {
        UserInfo add = new UserInfo("add");
        UserInfo delete = new UserInfo("delete");
        mUsers.add(delete);
        mUsers.add(0,add);
    }

    @Override
    public int getCount() {
        return mUsers == null ? 0 :mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder =null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext , R.layout.item_groupdetail ,null);

            holder.photo = (ImageView) convertView.findViewById(R.id.im_group_detail_photo);
            holder.delete = (ImageView)convertView.findViewById(R.id.im_group_detail_delete);
            holder.name = (TextView)convertView.findViewById(R.id.tv_group_detail_name);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();

        }

        //获取当前item的值
        UserInfo userInfo = mUsers.get(position);
        //显示数据
        if(mIsCanModify){
            //群主||开放群
            if(position == getCount()-1){
                if(mIsDeleteModel){
                    convertView.setVisibility(View.INVISIBLE);
                }else {
                    convertView.setVisibility(View.VISIBLE);

                    holder.photo.setImageResource(R.drawable.minusmember);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.INVISIBLE);
                }
            }else if(position ==getCount()-2){

                if(mIsDeleteModel){
                    convertView.setVisibility(View.INVISIBLE);
                }else {
                    convertView.setVisibility(View.VISIBLE);

                    holder.photo.setImageResource(R.drawable.add_bold);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.INVISIBLE);
                }
            }else {
                convertView.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);
                holder.name.setText(userInfo.getName());
                holder.photo.setImageResource(R.drawable.default_people);
                if(mIsDeleteModel){
                    holder.delete.setVisibility(View.VISIBLE);

                }else {
                    holder.delete.setVisibility(View.GONE);
                }

            }
            //点击事件
            if(position == getCount() - 1){
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mIsDeleteModel){
                            mIsDeleteModel = true;
                            notifyDataSetChanged();
                        }
                    }
                });
            }else if(position == getCount() -2){//加号
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.onAddMembers();
                    }
                });
            }else {//减号
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.onDeleteMember(userInfo);
                    }
                });
            }

        }else {
            //普通的群成员
            if(position == getCount() -1||position == getCount() - 2){
                convertView.setVisibility(View.GONE);
            }else {
                convertView.setVisibility(View.VISIBLE);

                holder.name.setText(userInfo.getName());

                holder.photo.setImageResource(R.drawable.default_people);

                holder.delete.setVisibility(View.GONE);

            }
        }
        //返回View
        return convertView;
    }

    private class ViewHolder{

        private ImageView photo;
        private ImageView delete;
        private TextView name;
    }

    public interface OnGroupDetailListener{
        //添加群成员
        void onAddMembers();
        //删除群成员
        void onDeleteMember(UserInfo user);
    }
}
