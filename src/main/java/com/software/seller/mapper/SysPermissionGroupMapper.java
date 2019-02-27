package com.software.seller.mapper;

import com.software.seller.model.SysPermissionGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "SysPermissionGroupMapper")
public interface SysPermissionGroupMapper {
    //新增
    public Long insert(SysPermissionGroup SysPermissionGroup);

    //更新
    public void update(SysPermissionGroup SysPermissionGroup);

    //通过对象进行查询
    public SysPermissionGroup select(SysPermissionGroup SysPermissionGroup);

    //通过id进行查询
    public SysPermissionGroup selectById(@Param("id") Long id);

    //查询全部
    public List<SysPermissionGroup> selectAll();

    //查询数量
    public int selectCounts();

    boolean isExistGroupName(@Param("name") String name);

    boolean isExistGroupCode(@Param("code") String code);

    void deleteById(@Param("id") long id);

    //删除status==2的记录
    void shrink();
}
