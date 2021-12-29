package com.agroup.store.req;

import java.util.Date;

public class PurchaseFormReq {

    private Integer goodsId;

    private Integer buyerId;

    private Date tradingDate;

    private String tradingLocation;

    private Float payment;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Date getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(Date tradingDate) {
        this.tradingDate = tradingDate;
    }

    public String getTradingLocation() {
        return tradingLocation;
    }

    public void setTradingLocation(String tradingLocation) {
        this.tradingLocation = tradingLocation;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "PurchaseFormReq{" +
                "goodsId=" + goodsId +
                ", buyerId=" + buyerId +
                ", tradingDate=" + tradingDate +
                ", tradingLocation='" + tradingLocation + '\'' +
                ", payment=" + payment +
                '}';
    }
}
