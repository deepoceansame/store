package com.agroup.store.req;

public class DesiredGoodsListReq extends PageReq{
    private Integer accountId;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "DesiredGoodsListReq{" +
                "accountId=" + accountId +
                "} " + super.toString();
    }
}
