package com.software.seller.mapper;

import com.software.seller.model.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "SysRoleMapper")
public interface SysRoleMapper {
    //新增
    Long insert(SysRole SysRole);

    //更新
    void update(SysRole SysRole);

    void deleteById(@Param("id") Long id);

    //通过对象进行查询
    SysRole select(SysRole SysRole);

    //通过id进行查询
    SysRole selectById(@Param("id") Long id);

    List<String> selectNameByUserId(@Param("id") Long id);

    List<SysRole> selectByOrgId(@Param("orgIds") List<Long> orgIds);

    //查询全部
    List<SysRole> selectAll();
    List<SysRole> selectAllSpecial(
            @Param("name") String name,
            @Param("description") String description,
            @Param("permissionCodes") List<String> codes,
            @Param("groupIds") List<Long> groupIds);

    //查询数量
    int selectCounts();

    boolean isExsitName(@Param("name") String name);

    boolean isExsitNameExcludeId(@Param("id") long id, @Param("name") String name);

    //删除status==2的记录
    void shrink();
}
