package com.software.seller.bean;

import org.springframework.stereotype.Component;
import java.util.List;

@Component(value="SysOrganizationListBean")
public class SysOrganizationListBean {
    private Long id;

    // name :名称
    private String name;

    // fullName :全称
    private String fullName;

    // description :描述
    private String description;

    // parent_id :
    private Long parentId;

    private Integer isFinal;

    // rank :排序
    private Long rank;

    private List<Long> roleIds;

    private String roles;

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getRoles() {
        return this.roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public List<Long> getRoleIds() {
        return this.roleIds;
    }
    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public void normalize() {
        if (null == this.name) { this.name = ""; }
        if (null == this.fullName) { this.fullName = ""; }
        if (null == this.description) { this.description = ""; }
    }
}
