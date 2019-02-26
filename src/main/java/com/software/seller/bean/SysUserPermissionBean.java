package com.software.seller.bean;

import java.util.List;

public class SysUserPermissionBean {

    private Long sysUserId;
    private List<Long> rolePermissionIds;

    public Long getSysUserId() {
        return sysUserId;
    }
    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public List<Long> getRolePermissionIds() { return rolePermissionIds; }
    public void setrolePermissionIds(List<Long> rolePermissionIds) {
        this.rolePermissionIds = rolePermissionIds;
    }

    public void normalize() {
        if (null == this.sysUserId) { this.sysUserId = 0L; }

    }
}
