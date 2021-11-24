package com.agroup.store.req;

public class GetMessageReq {
    private Integer senderid;

    private Integer receiverid;

    private Integer goodsid;

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

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    @Override
    public String toString() {
        return "GetMessageReq{" +
                "senderid=" + senderid +
                ", receiverid=" + receiverid +
                ", goodsid=" + goodsid +
                '}';
    }
}
