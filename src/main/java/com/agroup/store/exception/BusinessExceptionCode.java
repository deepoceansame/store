package com.agroup.store.exception;

public enum BusinessExceptionCode {
    MAIL_BEEN_USED("邮箱已被使用"),
    WRONG_PASS_OR_ACCOUNT("用户名不存在或密码错误"),
    DUP_PASSWORD_DIFFERENT("确认密码不一样");

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
