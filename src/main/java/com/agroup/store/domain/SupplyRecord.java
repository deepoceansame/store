package com.agroup.store.domain;

import java.util.Date;

public class SupplyRecord {
    private Integer sellerId;

    private Integer goodsId;

    private Boolean effective;

    private Date tradingDate;

    private String tradingLocation;

    private Float payment;

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
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
        return "SupplyRecord{" +
                "sellerId=" + sellerId +
                ", goodsId=" + goodsId +
                ", effective=" + effective +
                ", tradingDate=" + tradingDate +
                ", tradingLocation='" + tradingLocation + '\'' +
                ", payment=" + payment +
                '}';
    }
}
