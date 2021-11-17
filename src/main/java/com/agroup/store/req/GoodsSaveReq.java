package com.agroup.store.req;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class GoodsSaveReq {
    private Integer id;

    @NotNull(message = "[商品名]不能为空")
    private String name;

    private String description;

    private Integer categoryId;

    private Integer accountId;

    private Float price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public GoodsSaveReq() {
    }

    @Override
    public String toString() {
        return "GoodsSaveReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", accountId=" + accountId +
                ", price=" + price +
                '}';
    }
}
