package com.agroup.store.req;

public class GetMessagedReq {
    private Integer senderid;

    private Integer receiverid;

    private Integer desiredgoodsid;

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

    @Override
    public String toString() {
        return "GetMessagedReq{" +
                "senderid=" + senderid +
                ", receiverid=" + receiverid +
                ", desiredgoodsid=" + desiredgoodsid +
                '}';
    }
}
