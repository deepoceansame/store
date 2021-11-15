package com.agroup.store.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class AccountLoginReq {
    @NotEmpty(message = "【邮箱】不能为空")
    private String mail;

    @NotEmpty(message = "【密码】不能为空")
    private String password;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AccountLoginReq{" +
                "mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
