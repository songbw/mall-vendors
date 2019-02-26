package  com.software.seller.mapper;

import com.software.seller.model.SysUserRoleOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "SysUserRoleOrganizationMapper")
public interface SysUserRoleOrganizationMapper {
    //新增
    Long insert(SysUserRoleOrganization SysUserRoleOrganization);

    //更新
    void update(SysUserRoleOrganization SysUserRoleOrganization);

    //通过对象进行查询
    SysUserRoleOrganization select(SysUserRoleOrganization SysUserRoleOrganization);

    //通过id进行查询
    SysUserRoleOrganization selectById(@Param("id") Long id);

    //查询全部
    List<SysUserRoleOrganization> selectAll();

    //查询数量
    int selectCounts();

    void deleteUserId(@Param("userId") long userId);

    SysUserRoleOrganization selectByUserId(@Param("userId") Long userId);

    List<Long> selectByRoleOrganizationId(@Param("roleOrganizationId") long roleOrganizationId);

    boolean isExistByUserId(@Param("roleOrganizationId") long roleOrganizationId, @Param("userId") Long userId);

    //删除status==2的记录
    void shrink();
}
