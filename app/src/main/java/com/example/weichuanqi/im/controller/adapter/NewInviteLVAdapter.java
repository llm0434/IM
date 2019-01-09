package com.example.weichuanqi.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.model.bean.Invite;
import com.example.weichuanqi.im.model.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请信息listView适配器
 */

public class NewInviteLVAdapter extends BaseAdapter {

    private Context mContext;

    private List<Invite> mInfoList = new ArrayList<>();

    private OnInviteListener onInviteListener;
    private Invite invitionInfo;

    public NewInviteLVAdapter(Context context, OnInviteListener listener) {
        this.mContext = context;
        this.onInviteListener = listener;
    }

    //刷新数据
    public void refresh(List<Invite> list) {
        if (list != null && list.size() >= 0) {

            mInfoList.clear();
            mInfoList.addAll(list);
        }
    }


    @Override
    public int getCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.获取或创建viewholder
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.invite_item, null);

            viewHolder.invite_id = (TextView) convertView.findViewById(R.id.tv_new_invite_id);
            viewHolder.invite_reason = (TextView) convertView.findViewById(R.id.tv_new_invite_reason);
            viewHolder.iv_agree = (ImageView) convertView.findViewById(R.id.iv_new_invite_agree);
            viewHolder.iv_refuse = (ImageView) convertView.findViewById(R.id.iv_new_invite_refuse);
            viewHolder.iv_invite_pic = (ImageView) convertView.findViewById(R.id.iv_new_invite_icon);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        //2.获取当前item的数据
        invitionInfo = mInfoList.get(position);


        //3.显示当前item数据
        User userInfo = invitionInfo.getUser();
        if (userInfo != null) {  //是好友
            //邀请人ID
            viewHolder.invite_id.setText(invitionInfo.getUser().getName());

            //设置隐藏
            viewHolder.iv_agree.setVisibility(View.INVISIBLE);
            viewHolder.iv_refuse.setVisibility(View.INVISIBLE);
            //邀请原因
            if (invitionInfo.getStatus() == Invite.InvitationStatus.NEW_INVITE) { //新的邀请

                if (invitionInfo.getReason() == null) {
                    viewHolder.invite_reason.setText("添加好友");
                } else {
                    viewHolder.invite_reason.setText(invitionInfo.getReason());
                }
                //  设置显示
                viewHolder.iv_agree.setVisibility(View.VISIBLE);
                viewHolder.iv_refuse.setVisibility(View.VISIBLE);


            } else if (invitionInfo.getStatus() == Invite.InvitationStatus.INVITE_ACCEPT) { //接收邀请

                if (invitionInfo.getReason() == null) {
                    viewHolder.invite_reason.setText("接受邀请");
                } else {
                    viewHolder.invite_reason.setText(invitionInfo.getReason());
                }


            } else if (invitionInfo.getStatus() == Invite.InvitationStatus.INVITE_ACCEPT_BY_PEER) { //邀请被接收
                if (invitionInfo.getReason() == null) {
                    viewHolder.invite_reason.setText("同意您的邀请");
                } else {
                    viewHolder.invite_reason.setText(invitionInfo.getReason());
                }
            }


            /**
             *      处理同意和不同意的点击事件
             */

            //同意邀请的监听
            viewHolder.iv_agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onInviteListener.onAccept(invitionInfo);
                }
            });
            //拒绝邀请的监听
            viewHolder.iv_refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInviteListener.onRefuse(invitionInfo);
                }
            });


        } else {  //是群组
            viewHolder.invite_id.setText(invitionInfo.getGroup().getInviter());

            viewHolder.iv_agree.setVisibility(View.GONE);
            viewHolder.iv_refuse.setVisibility(View.GONE);

            viewHolder.iv_invite_pic.setImageResource(R.drawable.group_icon);
            // 显示原因
            switch(invitionInfo.getStatus()){
                // 您的群申请请已经被接受
                case GROUP_APPLICATION_ACCEPTED:
                    viewHolder.invite_reason.setText("您的群申请请已经被接受");
                    break;
                //  您的群邀请已经被接收
                case GROUP_INVITE_ACCEPTED:
                    viewHolder.invite_reason.setText("您的群邀请已经被接收");
                    break;

                // 你的群申请已经被拒绝
                case GROUP_APPLICATION_DECLINED:
                    viewHolder.invite_reason.setText("你的群申请已经被拒绝");
                    break;

                // 您的群邀请已经被拒绝
                case GROUP_INVITE_DECLINED:
                    viewHolder.invite_reason.setText("您的群邀请已经被拒绝");
                    break;

                // 您收到了群邀请
                case NEW_GROUP_INVITE:
                    viewHolder.iv_agree.setVisibility(View.VISIBLE);
                    viewHolder.iv_refuse.setVisibility(View.VISIBLE);

                    // 接受邀请
                    viewHolder.iv_agree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onInviteListener.onInviteAccept(invitionInfo);
                        }
                    });

                    // 拒绝邀请
                    viewHolder.iv_refuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onInviteListener.onInviteReject(invitionInfo);
                        }
                    });

                    viewHolder.invite_reason.setText("您收到了群邀请");
                    break;

                // 您收到了群申请
                case NEW_GROUP_APPLICATION:
                    viewHolder.iv_agree.setVisibility(View.VISIBLE);
                    viewHolder.iv_refuse.setVisibility(View.VISIBLE);

                    // 接受申请
                    viewHolder.iv_agree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onInviteListener.onApplicationAccept(invitionInfo);
                        }
                    });

                    // 拒绝申请
                    viewHolder.iv_refuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onInviteListener.onApplicationReject(invitionInfo);
                        }
                    });

                    viewHolder.invite_reason.setText("您收到了群申请");
                    break;

                // 你接受了群邀请
                case GROUP_ACCEPT_INVITE:
                    viewHolder.invite_reason.setText("你接受了群邀请");
                    break;

                // 您批准了群申请
                case GROUP_ACCEPT_APPLICATION:
                    viewHolder.invite_reason.setText("您批准了群申请");
                    break;

                // 您拒绝了群邀请
                case GROUP_REJECT_INVITE:
                    viewHolder.invite_reason.setText("您拒绝了群邀请");
                    break;

                // 您拒绝了群申请
                case GROUP_REJECT_APPLICATION:
                    viewHolder.invite_reason.setText("您拒绝了群申请");
                    break;
            }
        }


        return convertView;
    }


    class ViewHolder {
        private TextView invite_id;
        private TextView invite_reason;

        private ImageView iv_agree;
        private ImageView iv_refuse;

        private ImageView iv_invite_pic;

    }


    /**
     * 处理接受和拒绝的接口方法
     */

    public interface OnInviteListener {
        //接受邀请的方法
        void onAccept(Invite info);

        //拒绝邀请的方法
        void onRefuse(Invite info);


        // 接受邀请按钮处理
        void onInviteAccept(Invite invationInfo);
        // 拒绝邀请按钮处理
        void onInviteReject(Invite invationInfo);

        // 接受申请按钮处理
        void onApplicationAccept(Invite invationInfo);
        // 拒绝申请按钮处理
        void onApplicationReject(Invite invationInfo);

    }


}
