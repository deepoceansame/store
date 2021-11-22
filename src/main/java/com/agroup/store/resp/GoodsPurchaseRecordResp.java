package com.agroup.store.resp;

import com.agroup.store.domain.Goods;

public class GoodsPurchaseRecordResp extends Goods {
    private boolean effective;

    public boolean isEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    @Override
    public String toString() {
        return "GoodsPurchaseRecordResp{" +
                "effective=" + effective +
                "} " + super.toString();
    }
}
