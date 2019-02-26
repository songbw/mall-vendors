package com.software.seller.service;

import com.software.seller.util.PageInfo;
import com.software.seller.model.SysPermission;
import com.software.seller.bean.SysPermissionBean;
import com.software.seller.model.SysPermissionGroup;

import java.util.List;

public interface ISysPermissionService {
    boolean isExistName(long groupId, String name);

    boolean isExistCode(long groupId, String code);

    long insertPermission(SysPermission sysPermission);

    SysPermission selectById(long id);

    void update(SysPermission sysPermission);

    boolean isExistNameExcludeId(long id, long groupId, String name);

    boolean isExistCodeExcludeId(long id, long groupId, String code);

    PageInfo<SysPermissionBean> selectPage(int page, int rows, String sort, String order, String name, String description, String code, Long sysPermissionGroupId);

    boolean isExistGroupName(String name);

    long insertPermissionGroup(SysPermissionGroup sysPermissionGroup);

    List<SysPermissionGroup> selectGroup();

    List<SysPermission> selectByGroupId(Long groupId);

    List<SysPermission> selectByUserId(Long userId);

    List<String> selectCodeByUserId(Long userId);

    List<SysPermission> selectAll();

    List<Long> selectIdByRoleId(Long roleId);

    boolean deleteById(long id);

    boolean deleteGroupById(long id);

    SysPermissionGroup selectGroupById(long groupId);

    void updateGroupById(SysPermissionGroup sysPermissionGroup);
}
