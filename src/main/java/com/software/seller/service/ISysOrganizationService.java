package com.software.seller.service;

import com.software.seller.util.PageInfo;
import com.software.seller.bean.SysOrganizationTree;
import com.software.seller.bean.SysOrganizationListBean;
import com.software.seller.model.SysOrganization;

import java.util.List;

public interface ISysOrganizationService {
    Long insertOrganization(SysOrganization sysOrganization, List<Long> roleIds);

    boolean deleteOrganization(long id);

    void updateOrganization(SysOrganization organization, List<Long> roleIds);

    PageInfo<SysOrganizationTree> selectPage(int page, int row);

    SysOrganizationTree selectSysOrganizationTree(long id);

    List<SysOrganizationTree> selectChildrenTreeList(long id);

    List<Long> selectChildrenListByUserId(long userId);

    boolean isExistFullName(String fullName);

    boolean isExistName(String name);

    SysOrganization selectOrganization(long id);

    boolean isExistFullNameExcludeId(long id, String fullName);

    List<SysOrganizationListBean> selectList();

    List<SysOrganization> selectChildrenByRank(Long parentId, Long rank);

    List<Long> selectAllRank();

    PageInfo<SysOrganizationListBean> selectOrgPage(int page, int rows, String sort, String order, String name, String description, Long rank, List<Long> roleIds);
}
