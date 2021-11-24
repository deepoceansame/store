package com.agroup.store.domain;

import java.sql.Timestamp;

public class Message {

    private Integer senderid;

    private Integer receiverid;

    private Integer goodsid;

    private Timestamp sendtime;

    private Integer type;

    private String content;

    public Integer getSenderid() {
        return senderid;
    }

    public void setSenderid(Integer senderid) {
        this.senderid = senderid;
    }

    public Timestamp getSendtime() {
        return sendtime;
    }

    public void setSendtime(Timestamp sendtime) {
        this.sendtime = sendtime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(Integer receiverid) {
        this.receiverid = receiverid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderid=" + senderid +
                ", receiverid=" + receiverid +
                ", goodsid=" + goodsid +
                ", sendtime=" + sendtime +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
