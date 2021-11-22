package com.agroup.store.req;
import javax.validation.constraints.NotEmpty;

public class RequestGoodsReq {
    private Integer accountId;
    private Integer goodId;

    @Override
    public String toString() {
        return "RequestGoodsReq{" +
                "accountId=" + accountId +
                ", goodId=" + goodId +
                '}';
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }
}
