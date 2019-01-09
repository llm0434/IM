package com.example.weichuanqi.im.model.bean;

public class Group {


    private String groupId;
    private String groupName;
    private String inviter;


    public Group() {
    }

    public Group(String groupId, String groupName, String inviter) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.inviter = inviter;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", inviter='" + inviter + '\'' +
                '}';
    }
}
