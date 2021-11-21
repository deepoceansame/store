package com.agroup.store.req;

public class PurchaseRecordReq extends PageReq{
    private Integer accountId;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "PurchaseRecordReq{" +
                "accountId=" + accountId +
                "} " + super.toString();
    }
}
