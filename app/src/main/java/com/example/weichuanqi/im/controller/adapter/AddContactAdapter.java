package com.example.weichuanqi.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.model.bean.AddContactBean;

import java.util.ArrayList;
import java.util.List;

public class AddContactAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddContactBean> mList = new ArrayList<>();
    private List<String> mExist_members = new ArrayList<>();
    public AddContactAdapter(Context context,List<AddContactBean> list,List<String> members) {
        this.mContext = context;

        if (list != null && list.size() >= 0){
            mList.clear();
            mList.addAll(list);
        }
        mExist_members.clear();
        mExist_members.addAll(members);

    }

    @Override
    public int getCount() {
        return mList == null ? 0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.add_contact_item,null);
            viewHolder.cb_add_contact = (CheckBox) convertView.findViewById(R.id.cb_add_contact);
            viewHolder.tv_add_contact_name = (TextView) convertView.findViewById(R.id.tv_add_contact_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AddContactBean addContactBean = mList.get(position);
        viewHolder.cb_add_contact.setChecked(addContactBean.isChecked());
        viewHolder.tv_add_contact_name.setText(addContactBean.getUser().getName());

        //如果联系人表中存在你的联系人，默认将CheckBox勾选
        if (mExist_members.contains(addContactBean.getUser().getId())){
            viewHolder.cb_add_contact.setChecked(true);
            addContactBean.setChecked(true);
        }

        return convertView;
    }

    /**
     * 获取选中的联系人
     * @return
     */
    public List<String> getCheckedContact() {
        //1.定义集合
        List<String> contacts = new ArrayList<>();
        //2.遍历集合
        for (AddContactBean con:mList) {
            //3.判断是否选中
            if (con.isChecked()){
                //4.添加到id集合中
                contacts.add(con.getUser().getId());
            }
        }
        //5.返回
        return contacts;
    }

    class ViewHolder{
        private CheckBox cb_add_contact;
        private TextView tv_add_contact_name;
    }


}
