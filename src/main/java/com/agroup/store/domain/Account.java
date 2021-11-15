package com.agroup.store.domain;

public class Account {
    private Integer id;

    private String name;

    private String password;

    private String mail;

    private String recvaddress;

    private String avator;

    private String qq;

    private Float money;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRecvaddress() {
        return recvaddress;
    }

    public void setRecvaddress(String recvaddress) {
        this.recvaddress = recvaddress;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", password=").append(password);
        sb.append(", mail=").append(mail);
        sb.append(", recvaddress=").append(recvaddress);
        sb.append(", avator=").append(avator);
        sb.append(", qq=").append(qq);
        sb.append(", money=").append(money);
        sb.append("]");
        return sb.toString();
    }
}