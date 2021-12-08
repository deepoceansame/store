package com.agroup.store.req;

public class ParticiSupplyReq {
    Integer desiredgoodsId;

    Integer accountId;

    public Integer getDesiredgoodsId() {
        return desiredgoodsId;
    }

    public void setDesiredgoodsId(Integer desiredgoodsId) {
        this.desiredgoodsId = desiredgoodsId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accounId) {
        this.accountId = accounId;
    }

    @Override
    public String toString() {
        return "ParticiSupplyReq{" +
                "desiredgoodsId=" + desiredgoodsId +
                ", accountId=" + accountId +
                '}';
    }
}
