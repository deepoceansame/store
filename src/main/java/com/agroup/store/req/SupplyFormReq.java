package com.agroup.store.req;

import java.util.Date;

public class SupplyFormReq {
    private Integer desiredGoodsId;

    private Integer sellerId;

    private Date tradingDate;

    private String tradingLocation;

    private Float payment;

    public Integer getDesiredGoodsId() {
        return desiredGoodsId;
    }

    public void setDesiredGoodsId(Integer desiredGoodsId) {
        this.desiredGoodsId = desiredGoodsId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
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
        return "SupplyFormReq{" +
                "desiredGoodsId=" + desiredGoodsId +
                ", sellerId=" + sellerId +
                ", tradingDate=" + tradingDate +
                ", tradingLocation='" + tradingLocation + '\'' +
                ", payment=" + payment +
                '}';
    }
}
