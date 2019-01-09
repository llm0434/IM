package com.example.weichuanqi.im.model.bean;

/**
 *  会话bean类
 */

public class Conversation {

    private String send_name;
    private String send_id;
    private String receive_name;
    private String receive_id;
    private String chat_type;
    private boolean is_group;

    public boolean isIs_group() {
        return is_group;
    }

    public void setIs_group(boolean is_group) {
        this.is_group = is_group;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Conversation() {
    }

    public String getSend_name() {
        return send_name;
    }

    public void setSend_name(String send_name) {
        this.send_name = send_name;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public String getReceive_id() {
        return receive_id;
    }

    public void setReceive_id(String receive_id) {
        this.receive_id = receive_id;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }


    @Override
    public String toString() {
        return "Conversation{" +
                "send_name='" + send_name + '\'' +
                ", send_id='" + send_id + '\'' +
                ", receive_name='" + receive_name + '\'' +
                ", receive_id='" + receive_id + '\'' +
                ", chat_type='" + chat_type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
