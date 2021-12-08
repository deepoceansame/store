package com.agroup.store.req;

public class QuitBuyReq {
    private Integer buyerId;

    private Integer GoodsId;

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(Integer goodsId) {
        GoodsId = goodsId;
    }

    @Override
    public String toString() {
        return "QuitBuyReq{" +
                "buyerId=" + buyerId +
                ", GoodsId=" + GoodsId +
                '}';
    }
}
