package com.agroup.store.req;

public class QuitBuyReq {
    private Integer buyerId;

    private Integer goodsId;

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "QuitBuyReq{" +
                "buyerId=" + buyerId +
                ", GoodsId=" + goodsId +
                '}';
    }
}
