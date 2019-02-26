package com.software.seller.model;

public class SysUserPermission {

    // sys_user_id :
    private Long sysUserId;

    // sys_permission_id :
    private Long sysPermissionId;

    // is_final :
    private Integer isFinal;

    // rank :排序
    private Long rank;

    // status :数据状态,1:正常,2:删除
    private Integer status;

    private java.util.Date createTime;
    private java.util.Date updateTime;
    private Long createBy; // create_by :创建人
    private Long updateBy; // update_by :更热人


    public Long getSysUserId() {
        return sysUserId;
    }
    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Long getSysPermissionId() {
        return sysPermissionId;
    }
    public void setSysPermissionId(Long sysPermissionId) {
        this.sysPermissionId = sysPermissionId;
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

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public java.util.Date getCreateTime() { return createTime; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }

    public java.util.Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(java.util.Date updateTime) { this.updateTime = updateTime; }

    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }

    public Long getUpdateBy() { return updateBy; }
    public void setUpdateBy(Long updateBy) { this.updateBy = updateBy; }

    @Override
    public String toString() {
        return "SysUserPermission{" +
                ", sysUserId='" + sysUserId + '\'' +
                ", sysPermissionId='" + sysPermissionId + '\'' +
                ", isFinal='" + isFinal + '\'' +
                ", rank='" + rank + '\'' +
                ", status=" + status +
                '}';
    }
}
