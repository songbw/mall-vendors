package com.software.seller.bean;

public class UserRegistryBean {
    private String loginName;
    private String zhName;// zh_name :中文名
    private String enName;// en_name :英文名
    private Integer sex;
    private String birth;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String code;

    public void normalize() {
        if (null == this.address) { this.address = ""; }
        if (null == this.birth ) { this.birth = ""; }
        if (null == this.code) { this.code = ""; }
        if (null == this.email) { this.email= ""; }
        if (null == this.phone) { this.phone = ""; }
        if (null == this.sex) { this.sex = 0; }
        if (null == this.enName) { this.enName = ""; }
        if (null == this.zhName) { this.zhName = ""; }
    }

    public String getLoginName() { return this.loginName; }
    public void setLoginName(String loginName) { this.loginName = loginName; }

    public String getZhName() { return this.zhName; }
    public void setZhName(String zhName) { this.zhName = zhName; }

    public String getEnName() { return this.enName; }
    public void setEnName(String enName) { this.enName = enName; }


    public String getBirth() { return this.birth; }
    public void setBirth(String birth) { this.birth = birth; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return this.phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return this.address; }
    public void setAddress(String address) { this.address = address; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

    public String getCode() { return this.code; }
    public void setCode(String code) { this.code = code; }

    public Integer getSex() { return this.sex;}
    public void setSex(Integer sex) { this.sex = sex; }

}
