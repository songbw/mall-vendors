package com.software.seller.service;

import com.software.seller.util.PageInfo;
import com.software.seller.bean.UserRolePermissionBean;
import com.software.seller.model.SysRole;
import com.software.seller.bean.SysRoleBean;
import java.util.List;

public interface ISysRoleService {
    boolean isExsitRoleName(String name);

    void insertRole(SysRole sysRole, List<Long> permissionIds);

    void updateRole(SysRole sysRole, List<Long> permissionIds);

    boolean isExsitRoleNameExcludeId(long id, String name);

    SysRoleBean selectById(long id);

    PageInfo<SysRoleBean> selectPage(int page, int row);

    void deleteRole(SysRole sysRole);

    void deleteRoleById(Long id);

    List<String> selectRoleByUserId(long userId);

    List<UserRolePermissionBean> selectSpecial(UserRolePermissionBean bean);

    List<SysRoleBean> selectByOrgIds(List<Long> orgIds);
}
