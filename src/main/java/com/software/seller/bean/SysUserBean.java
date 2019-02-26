package com.software.seller.bean;

import java.util.List;

public class SysUserBean {

    private Long id;
    private String loginName;
    private String zhName;// zh_name :中文名
    private String enName;// en_name :英文名
    private Integer sex;
    private String birth;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String createTime;
    private String updateTime;
    private String createBy;
    private String updateBy;
    private Long organizationId;
    private List<Long> permissionIds;
    private List<String> permissionCodes;

    public void normalize() {
        if (null == this.address) { this.address = ""; }
        if (null == this.birth ) { this.birth = ""; }
        if (null == this.createBy) { this.createBy = ""; }
        if (null == this.createTime) { this.createTime = ""; }
        if (null == this.updateTime) { this.updateTime = ""; }
        if (null == this.email) { this.email= ""; }
        if (null == this.phone) { this.phone = ""; }
        if (null == this.sex) { this.sex = 0; }
        if (null == this.enName) { this.enName = ""; }
        if (null == this.zhName) { this.zhName = ""; }
        if (null == this.updateBy) { this.updateBy = ""; }
    }

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

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

    public String getCreateTime() {return this.createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getUpdateTime() { return this.updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return this.createBy; }
    public void setCreateBy(String createBy) { this.createBy =  createBy; }

    public String getUpdateBy() { return this.updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Integer getSex() { return this.sex;}
    public void setSex(Integer sex) { this.sex = sex; }

    public List<Long> getPermissionIds() { return this.permissionIds; }
    public void setPermissionIds(List<Long> permissions) { this.permissionIds = permissions; }

    public List<String> getPermissionCodes() { return this.permissionCodes; }
    public void setPermissionCodes(List<String> codes) { this.permissionCodes = codes; }

    public Long getOrganizationId() { return this.organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

/*
    @Override
    public String toString() {
        return "SysUserDto{" +
                ", loginName='" + loginName + '\'' +
                ", zhName='" + zhName + '\'' +
                ", enName='" + enName + '\'' +
                ", sex=" + sex +
                ", birth='" + birth + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
      //          ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", permissions=" + permissions +
                ", userRoleOrganizations=" + userRoleOrganizations +
                '}';

    }*/

}
