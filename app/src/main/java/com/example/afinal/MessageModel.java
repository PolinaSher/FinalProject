package com.example.afinal;

public class MessageModel {

    private String msg;
    private String isSend;
    private String url;
    private long id;

    public MessageModel(String msg, String url,String isSend, long id) {
        this.msg = msg;
        this.isSend = isSend;
        this.url = url;
        this.id = id;
    }

    public MessageModel(String msg, String isSend, String url) {
        this.msg = msg;
        this.isSend = isSend;
        this.url = url;
    }


    public String getMsg() {
        return msg;
    }

    public String getIsSend() {
        return isSend;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
