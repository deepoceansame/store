package com.agroup.store.domain;

public class PurchaseRecord {
    private Integer buyerId;

    private Integer goodsId;

    private Boolean effective;

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

    public Boolean getEffective() {
        return effective;
    }

    public void setEffective(Boolean effective) {
        this.effective = effective;
    }

    @Override
    public String toString() {
        return "PurchaseRecord{" +
                "buyerId=" + buyerId +
                ", goodsId=" + goodsId +
                ", effective=" + effective +
                '}';
    }
}
