package com.software.seller.bean;

import com.software.seller.model.SysOrganization;
import com.software.seller.model.SysRole;

import java.io.Serializable;


public class UserRoleOriganization implements Serializable {
    private SysRole role;
    private SysOrganization organization;

    public UserRoleOriganization(SysRole role, SysOrganization organization) {
        this.role = role;
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "UserRoleOriganization{" +
                "role=" + role +
                ", organization=" + organization +
                '}';
    }

    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }

    public SysOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(SysOrganization organization) {
        this.organization = organization;
    }
}
