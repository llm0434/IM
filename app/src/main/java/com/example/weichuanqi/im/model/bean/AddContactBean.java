package com.example.weichuanqi.im.model.bean;

/**
 *  选择联系人的bean类
 */
public class AddContactBean {

    private User user;
    private boolean isChecked;

    public AddContactBean(User user, boolean isChecked) {
        this.user = user;
        this.isChecked = isChecked;
    }

    public AddContactBean() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
