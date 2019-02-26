package com.software.seller.bean;

public class LoginBean {

    private String username;
    private String password;
    private String oldPassword;
    private String code;

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

    public String getOldPassword() { return this.oldPassword; }
    public void setOldPassword(String password) { this.oldPassword = password; }

    public String getCode() { return this.code; }
    public void setCode(String code) { this.code = code; }

    public void normalize() {

        if (null == this.code) { this.code = ""; }
        if (null == this.password) { this.password = ""; }
        if (null == this.oldPassword) { this.oldPassword = ""; }
        if (null == this.username) { this.username = ""; }

    }
}
