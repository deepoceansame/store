package com.agroup.store.domain;

import java.sql.Timestamp;

public class Messaged {
    private Integer senderid;

    private Integer receiverid;

    private Integer desiredgoodsid;

    private Timestamp sendtime;

    private Integer type;

    private String content;

    public Integer getSenderid() {
        return senderid;
    }

    public void setSenderid(Integer senderid) {
        this.senderid = senderid;
    }

    public Integer getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(Integer receiverid) {
        this.receiverid = receiverid;
    }

    public Integer getDesiredgoodsid() {
        return desiredgoodsid;
    }

    public void setDesiredgoodsid(Integer desiredgoodsid) {
        this.desiredgoodsid = desiredgoodsid;
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

    @Override
    public String toString() {
        return "Messaged{" +
                "senderid=" + senderid +
                ", receiverid=" + receiverid +
                ", desiredgoodsid=" + desiredgoodsid +
                ", sendtime=" + sendtime +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
