package com.software.seller.mapper;

import com.software.seller.model.SysRoleOrganization;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component(value = "SysRoleOrganizationMapper")
public interface SysRoleOrganizationMapper {
    //新增
    Long insert(SysRoleOrganization SysRoleOrganization);

    //更新
    void update(SysRoleOrganization SysRoleOrganization);

    //通过对象进行查询
    SysRoleOrganization select(SysRoleOrganization SysRoleOrganization);

    //通过id进行查询
    SysRoleOrganization selectById(@Param("id") Long id);

    void deleteById(@Param("id") Long id);
    void deleteByOrgId(@Param("orgId") Long orgId);
    void deleteByOrgRoleId(@Param("orgId") Long orgId, @Param("roleId") Long roleId);

    SysRoleOrganization selectByOrgRoleId(@Param("orgId") Long orgId, @Param("RoleId") Long roleId);

    //查询全部
    List<SysRoleOrganization> selectAll();

    //查询数量
    int selectCounts();

    boolean isExistName(@Param("name") String name, @Param("parentId") long parentId);

    boolean isExistNameExcludeId(@Param("id") long id, @Param("name") String name, @Param("parentId") long parentId);

    List<SysRoleOrganization> selectChildren(@Param("parentId") long parentId);

    List<Long> selectIdByRoleId(@Param("roleId") long roleId);

    List<Long> selectRoleIdByOrgId(@Param("orgId") long orgId);

    List<Long> selectOrgIdByRoleId(@Param("roleId") long roleId);

    void recoveryRoleOrg(@Param("orgId") long orgId, @Param("roleId") long roleId, @Param("updateBy") long updateBy, @Param("updateTime") Date updateTime);
    //删除status==2的记录
    void shrink();
}
