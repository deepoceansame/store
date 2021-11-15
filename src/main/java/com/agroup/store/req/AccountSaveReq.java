package com.agroup.store.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class AccountSaveReq {
    private Integer id;

    @NotEmpty(message = "【邮箱】不能为空")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "【邮箱】格式错误")
    private String mail;

    @NotEmpty(message = "【昵称】不能为空")
    private String name;

    private String qq;

    @NotEmpty(message = "【密码】不能为空")
    private String password;

    @NotEmpty(message = "【密码】不能为空")
    private String repPassword;

    private String recvaddress;

    private Float money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepPassword() {
        return repPassword;
    }

    public void setRepPassword(String repPassword) {
        this.repPassword = repPassword;
    }

    public String getRecvaddress() {
        return recvaddress;
    }

    public void setRecvaddress(String recvaddress) {
        this.recvaddress = recvaddress;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "AccountSaveReq{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", name='" + name + '\'' +
                ", qq='" + qq + '\'' +
                ", password='" + password + '\'' +
                ", repPassword='" + repPassword + '\'' +
                ", recvaddress='" + recvaddress + '\'' +
                ", money=" + money +
                '}';
    }
}
