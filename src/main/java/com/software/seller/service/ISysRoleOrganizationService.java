package com.software.seller.service;


import com.software.seller.util.PageInfo;
import com.software.seller.bean.SysRoleOrganizationTree;
import com.software.seller.model.SysRoleOrganization;

import java.util.List;

public interface ISysRoleOrganizationService {
    boolean isExistName(String name, long parentId);

    long insertRoleOrganization(SysRoleOrganization roleOrganization);

    boolean isExistNameExcludeId(long id, String name, long parentId);

    void updateRoleOrganization(SysRoleOrganization roleOrganization);

    SysRoleOrganization selectRoleOrganizationById(long id);

    PageInfo<SysRoleOrganizationTree> selectPage(int page, int rows, long id);

    SysRoleOrganizationTree selectSysRoleOrganizationTree(long id);

    List<SysRoleOrganizationTree> selectSysRoleOrganizationTreeChildrenList(long id);

    List<Long> selectOrgIdByRoleId(Long roleId);

    void deleteById(Long id);
}
