package com.software.seller.bean;

public class SysPermissionBean {

    // id :
    private Long id;

    // name :名称
    private String name;

    // description :描述
    private String description;

    // code :编码
    private String code;

    // sys_permission_group :分组
    private Long sysPermissionGroupId;

    // is_final :是否可删除
    private Integer isFinal;

    // rank :排序
    private Long rank;

    private Integer status;

    @Override
    public String toString() {
        return "SysPermissionDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", code='" + code + '\'' +
                ", sysPermissionGroupId=" + sysPermissionGroupId +
                ", isFinal=" + isFinal +
                ", rank=" + rank +
                '}';
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public Long getSysPermissionGroupId() {
        return this.sysPermissionGroupId;
    }
    public void setSysPermissionGroupId(Long sysPermissionGroupId) {
        this.sysPermissionGroupId = sysPermissionGroupId;
    }

    public Integer getIsFinal() {
        return isFinal;
    }
    public void setIsFinal(Integer isFinal) {
        this.isFinal = isFinal;
    }

    public Long getRank() {
        return rank;
    }
    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public void normalize() {

        if (null == this.code) { this.code = ""; }
        if (null == this.name) { this.name = ""; }
        if (null == this.description) { this.description = ""; }

    }

}
