package com.software.seller.mapper;

import com.software.seller.model.SysUserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.*;

@Mapper
@Component(value="SysUserPermissionMapper")
public interface SysUserPermissionMapper {
    //新增
    Long insert(SysUserPermission SysUserPermission);

    //更新
    void update(SysUserPermission SysUserPermission);

    //通过对象进行查询
    SysUserPermission select(SysUserPermission SysUserPermission);

    //通过id进行查询
    SysUserPermission selectById(@Param("id") Long id);

    //查询全部
    List<SysUserPermission> selectAll();

    //查询数量
    int selectCounts();

    void deleteByUserId(@Param("userId") long userId);

    ArrayList<SysUserPermission> selectByUserId(Long userId);

    List<Long> selectPermissionByUserId(@Param("userId") Long userId);

    boolean isExistByUserId(@Param("userId") Long userId, @Param("permissionId") Long permissionId);

    //删除status==2的记录
    void shrink();
}
