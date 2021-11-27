package com.agroup.store.domain;

public class Goodsimage {
    private Integer goodsid;

    private String img;

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Goodsimage{" +
                "goodsid=" + goodsid +
                ", img='" + img + '\'' +
                '}';
    }
}
