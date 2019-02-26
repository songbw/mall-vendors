package com.software.seller.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import com.software.seller.model.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@Component(value = "SysPermissionMapper")
public interface SysPermissionMapper {
    //新增
    Long insert(SysPermission SysPermission);

    //更新
    void update(SysPermission SysPermission);

    //通过对象进行查询
    SysPermission select(SysPermission SysPermission);

    //通过id进行查询
    SysPermission selectById(@Param("id") Long id);

    //查询全部
    List<SysPermission> selectAll();

    List<SysPermission> selectSpecial(@Param("sort") String sort, @Param("order") String order,
                            @Param("name") String name, @Param("description") String description,
                            @Param("code") String code, @Param("sysPermissionGroupId") Long sysPermissionGroupId
                        );

    //查询数量
    int selectCounts();

    int selectSpecialCounts(@Param("sort") String sort, @Param("order") String order,
                                   @Param("name") String name, @Param("description") String description,
                                   @Param("code") String code, @Param("sysPermissionGroupId") Long sysPermissionGroupId
    );

    boolean isExistName(@Param("groupId") long groupId, @Param("name") String name);

    boolean isExistCode(@Param("groupId") long groupId, @Param("code") String code);

    boolean isExistNameExcludeId(@Param("id") long id, @Param("groupId") long groupId, @Param("name") String name);

    boolean isExistCodeExcludeId(@Param("id") long id, @Param("groupId") long groupId, @Param("code") String code);

    //List<String> selectCodeByIdList(@Param("idList") List<Long> idList);

    List<String> selectCodeByUserId(@Param("userId") Long userId);

    List<SysPermission> selectByUserId(@Param("userId") Long userId);

    List<SysPermission> selectByGroupId(@Param("groupId") long groupId);

    List<SysPermission> selectByRoleId(@Param("id") Long id);

    List<SysPermission> selectByCodes(@Param("codes") List<String> codes);

    void deleteById(@Param("id") long id);

    //删除status==2的记录
    void shrink();
}
