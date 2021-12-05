package com.agroup.store.req;

public class DesiredGoodsMainListReq {
    Integer page;

    Integer categoryId;

    String keyword;

    Integer AccountId;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getAccountId() {
        return AccountId;
    }

    public void setAccountId(Integer accountId) {
        AccountId = accountId;
    }

    @Override
    public String toString() {
        return "DesiredGoodsMainListReq{" +
                "page=" + page +
                ", categoryId=" + categoryId +
                ", keyword='" + keyword + '\'' +
                ", AccountId=" + AccountId +
                '}';
    }
}
