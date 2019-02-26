package com.software.seller.bean;

import java.util.List;

public class UserRolePermissionBean {

    private Long id;
    private String description;
    private String name;
    private Long rank;
    private Integer isFinal;
    private List<Long> permissionGroups;
    private List<String> permissionCodes;

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

    public List<Long> getPermissionGroups() { return this.permissionGroups; }
    public void setPermissionGroups(List<Long> groups) { this.permissionGroups = groups; }

    public List<String> getPermissionCodes() { return this.permissionCodes; }
    public void setPermissionCodes(List<String> codes) { this.permissionCodes = codes; }

}
