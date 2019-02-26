package com.software.seller.service;

import com.software.seller.bean.SysUserBean;
import com.software.seller.model.SysUser;
import com.software.seller.util.PageInfo;

//import java.io.Serializable;
import java.util.Date;
import java.util.List;
//import java.util.Map;

public interface ISysUserService {

    long insertUser(SysUser user, List<Long> permissionIds, Long roleOrganizationId);

    boolean isExistLoginName(String loginName);

    void deleteById(long id);

    SysUser selectById(long id);

    boolean isExistLoginNameExcludeId(long id, String loginName);

    void updateUser(SysUser user, List<Long> permissionIds, Long roleOrganizationId);

    PageInfo<SysUserBean> selectPage(int page, int rows, String sort, String order, String loginName, String zhName, String email, String phone, String address, Date createTimeStart, Date createTimeEnd, Date updateTimeStart, Date updateTimeEnd, Long rank);

    SysUser selectByLoginName(String loginName);

    void updateUserPermission(long id, List<Long> permissionIds);

    void updateUserRoleOrganization(long id, Long roleOrganizationId);

    long selectOrganizationId(long userId);

    long selectOrganizationRank(long userId);

    long getUserIdInToken(String authentication);

}
