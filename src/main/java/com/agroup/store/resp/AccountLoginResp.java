package com.agroup.store.resp;

public class AccountLoginResp{
    private Integer id;

    private String mail;

    private String name;

    private String token;

    private String avator;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avator;
    }

    public void setAvator(String avatar) {
        this.avator = avatar;
    }

    @Override
    public String toString() {
        return "AccountLoginResp{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", avatar='" + avator + '\'' +
                '}';
    }
}
