package com.example.testapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.model.bean.InvationInfo;
import com.example.testapplication.model.bean.UserInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class InviteAdapter extends BaseAdapter {
    private Context mContext;

    private List<InvationInfo> mInvationInfos = new ArrayList<>();

    private OnInviteListener mOnInviteListener;

    public InviteAdapter(Context context , OnInviteListener onInviteListener){
        mContext = context;
        mOnInviteListener = onInviteListener;
    }

    public void refresh(List<InvationInfo> invationInfos){

        if(invationInfos !=null && invationInfos.size() >=0){
            mInvationInfos.clear();
            mInvationInfos.addAll(invationInfos);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mInvationInfos == null ? 0 : mInvationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvationInfos.get(position);
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

            convertView = View.inflate(mContext, R.layout.item_invite,null);

            holder.name = (TextView) convertView.findViewById(R.id.invite_name);
            holder.reason = (TextView) convertView.findViewById(R.id.invite_reason);

            holder.accept = (Button)convertView.findViewById(R.id.invite_accept);
            holder.reject = (Button)convertView.findViewById(R.id.invite_reject);

            convertView.setTag(holder);


        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        InvationInfo invationInfo = mInvationInfos.get(position);

        UserInfo user = invationInfo.getUser();

        if(user != null){//联系人
            holder.name.setText(invationInfo.getUser().getName());

            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);

            if(invationInfo.getStatus()==InvationInfo.InvitationStatus.NEW_INVITE){

                if(invationInfo.getReason() == null){
                    holder.reason.setText("添加好友");
                }else{
                    holder.reason.setText(invationInfo.getReason());
                }

                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);


            }else if(invationInfo.getStatus() == InvationInfo.InvitationStatus.INVITE_ACCEPT){
                if(invationInfo.getReason() == null){
                    holder.reason.setText("接受邀请");
                }else{
                    holder.reason.setText(invationInfo.getReason());
                }
            }else if(invationInfo.getStatus() ==InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER){
                if(invationInfo.getReason()==null){
                    holder.reason.setText("邀请被接受");
                }else{
                    holder.reason.setText(invationInfo.getReason());
                }
            }

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onAccept(invationInfo);
                }
            });

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onReject(invationInfo);
                }
            });

        }else{//群组
            holder.name.setText(invationInfo.getGroup().getInvatePerson());

            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            switch (invationInfo.getStatus()){
                case GROUP_APPLICATION_ACCEPTED:
                    holder.reason.setText("您的群申请已被接受");
                    break;
                case GROUP_INVITE_ACCEPTED:
                    holder.reason.setText("您已成功加入该群");
                    break;
                case GROUP_APPLICATION_DECLINED:
                    holder.reason.setText("您的群申请已被拒绝");
                    break;
                case GROUP_INVITE_DECLINED:
                    holder.reason.setText("您的群邀请已被拒绝");
                    break;
                case NEW_GROUP_INVITE:
                    holder.reason.setText("您收到了群邀请");
                    holder.accept.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.VISIBLE);

                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onInviteAccept(invationInfo);
                        }
                    });

                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onInviteReject(invationInfo);
                        }
                    });
                    break;
                case NEW_GROUP_APPLICATION:
                    holder.reason.setText("您收到了群申请");
                    holder.accept.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.VISIBLE);

                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onApplicationAccept(invationInfo);
                        }
                    });

                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mOnInviteListener.onApplicationReject(invationInfo);
                        }
                    });

                    break;
                case GROUP_ACCEPT_INVITE:
                    holder.reason.setText("您接受了群邀请");
                    break;
                case GROUP_ACCEPT_APPLICATION:
                    holder.reason.setText("您批准了群加入");
                    break;
            }
        }


        return convertView;
    }

    private class ViewHolder{

        private TextView name;
        private TextView reason;
        private Button accept;
        private Button reject;
    }

    public interface OnInviteListener{
        //接受按钮点击事件
        void onAccept(InvationInfo invationInfo);
        //拒绝按钮点击事件
        void onReject(InvationInfo invationInfo);

        void onInviteAccept(InvationInfo invationInfo);
        void onInviteReject(InvationInfo invationInfo);

        void onApplicationAccept(InvationInfo invationInfo);
        void onApplicationReject(InvationInfo invationInfo);
    }
}
