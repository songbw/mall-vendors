package com.software.seller.mapper;


import com.software.seller.model.SysRolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component(value = "SysRolePermissionMapper")
public interface SysRolePermissionMapper {
    //新增
    Long insert(SysRolePermission SysRolePermission);

    //更新
    void update(SysRolePermission SysRolePermission);

    //通过对象进行查询
    SysRolePermission select(SysRolePermission SysRolePermission);

    //通过id进行查询
    SysRolePermission selectById(@Param("id") Long id);

    void deleteByRolePermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    //查询全部
    List<SysRolePermission> selectAll();

    //查询数量
    int selectCounts();

    void deleteByRoleId(@Param("roleId") Long roleId);

    List<SysRolePermission> selectByRoleId(@Param("roleId") Long roleId);

    List<Long> selectPermissionByUserId(@Param("userId") Long userId);

    List<Long> selectRoleByPermissions(@Param("permissionId") List<Long> idList);

    //删除status==2的记录
    void shrink();

    //permissionId List must NOT be empty
    void recoveryRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId, @Param("updateBy") long updateBy, @Param("updateTime") Date updateTime);
}
