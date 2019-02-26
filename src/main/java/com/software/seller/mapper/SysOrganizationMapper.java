package com.software.seller.mapper;

import com.software.seller.model.SysOrganization;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "SysOrganizationMapper")
public interface SysOrganizationMapper {
    //新增
    public Long insert(SysOrganization SysOrganization);

    //更新
    public void update(SysOrganization SysOrganization);

    //通过对象进行查询
    public SysOrganization select(SysOrganization SysOrganization);

    //通过id进行查询
    public SysOrganization selectById(@Param("id") Long id);

    //查询全部
    public List<SysOrganization> selectAll();

    //查询数量
    public int selectCounts();

    List<SysOrganization> selectChildren(@Param("parentId") long parentId);

    List<SysOrganization> selectChildrenByRank(@Param("parentId") long parentId, @Param("rank") long rank);

    boolean isExistFullName(@Param("fullName") String fullName);

    boolean isExistName(@Param("name") String name);

    boolean isExistFullNameExcludeId(@Param("id") long id, @Param("fullName") String fullName);

    List<Long> selectAllRank();

    List<SysOrganization> selectAllSpecial(
            @Param("sort") String sort, @Param("order") String order,
            @Param("name") String name, @Param("fullName") String fullName,
            @Param("description") String description, @Param("parentId") Long parentId,
            @Param("roleIds") List<Long> roleIds
            );

    //删除status==2的记录
    void shrink();
}
