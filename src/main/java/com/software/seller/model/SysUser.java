package com.software.seller.model;

public class SysUser {

    private Long id;
    private String loginName; // login_name :登陆名
    private String zhName; // zh_name :中文名
    private String enName; // en_name :英文名
    private Integer sex; // sex :性别
    private String birth;// birth :生日
    private String email;
    private String phone;
    private String address;
    private String password;
    private Long rank; // rank :排序
    private java.util.Date createTime;
    private java.util.Date updateTime;
    private Long createBy; // create_by :创建人
    private Long updateBy; // update_by :更热人
    private Integer status; // status :数据状态,1:正常,2:删除,3:禁用账号
    private Integer isFinal; // is_final :是否能修改

    public long getId()
    {
        return this.id;
    }
    public void setId(Long id) { this.id = id; }

    public String getLoginName() { return loginName; }
    public void setLoginName(String loginName) { this.loginName = loginName; }

    public String getZhName() { return zhName; }
    public void setZhName(String zhName) { this.zhName = zhName; }

    public String getEnName() {return enName; }
    public void setEnName(String enName) { this.enName = enName; }

    public Integer getSex() { return sex; }
    public void setSex(Integer sex) { this.sex = sex; }

    public String getBirth() { return birth; }
    public void setBirth(String birth) { this.birth = birth; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getRank() { return rank; }
    public void setRank(Long rank) { this.rank = rank; }

    public java.util.Date getCreateTime() { return createTime; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }

    public java.util.Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(java.util.Date updateTime) { this.updateTime = updateTime; }

    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }

    public Long getUpdateBy() { return updateBy; }
    public void setUpdateBy(Long updateBy) { this.updateBy = updateBy; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getIsFinal() { return isFinal; }
    public void setIsFinal(Integer isFinal) { this.isFinal = isFinal; }

    public String toString() {
        return "SysUser{" +
                "id='" + id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", zhName='" + zhName + '\'' +
                ", enName='" + enName + '\'' +
                ", sex='" + sex + '\'' +
                ", birth='" + birth + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", rank='" + rank + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", status='" + status + '\'' +
                ", isFinal=" + isFinal +
                '}';
    }
}
