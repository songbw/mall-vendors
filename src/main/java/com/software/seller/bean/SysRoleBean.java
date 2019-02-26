package com.software.seller.bean;

import org.springframework.stereotype.Component;

import java.util.List;

@Component(value="SysRoleBean")
public class SysRoleBean {

    // id :
    private Long id;

    // description :
    private String description;

    // name :
    private String name;

    // rank :排序
    private Long rank;

    // is_final :是否可删除
    private Integer isFinal;

    // create_time :创建时间
    private java.util.Date createTime;

    // update_time :更新时间
    private java.util.Date updateTime;

    // create_by :创建人id
    private Long createBy;

    // update_by :更新人id
    private Long updateBy;

    // status :数据状态,1:正常,2:删除
    private Integer status;

    private List<Long> sysPermissionIds;

    private List<Long> organizationId;

    public void normalize() {
        if (null == this.name) { this.name = ""; }
        if (null == this.description) { this.description = ""; }

    }

    public List<Long> getSysPermissionIds() {
        return sysPermissionIds;
    }
    public void setSysPermissions(List<Long> sysPermissions) {
        this.sysPermissionIds = sysPermissions;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getRank() {
        return rank;
    }
    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Integer getIsFinal() {
        return isFinal;
    }
    public void setIsFinal(Integer isFinal) {
        this.isFinal = isFinal;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Long> getOrganizationId() {
        return organizationId;
    }
    public void setOrganizationId(List<Long> ids) {
        this.organizationId = ids;
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", rank='" + rank + '\'' +
                ", isFinal='" + isFinal + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", status=" + status +
                '}';
    }
}
