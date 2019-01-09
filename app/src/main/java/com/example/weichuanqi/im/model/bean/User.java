package com.example.weichuanqi.im.model.bean;

/**
 *
 *
 *
 *
 */

public class User {

    private String id;          // 用户ID
    private String name;        // 用户名
    private String nick;        // 用户昵称
    private String image;       // 用户头像


    public User() {

    }

    public User(String id) {
        this.id = id;
        this.name = id;
        this.nick = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nick='" + nick + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
