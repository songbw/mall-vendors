package com.software.seller.mapper;

import com.software.seller.bean.UserDetailsBean;
import com.software.seller.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Date;

@Mapper
@Component(value = "SysUserMapper")
public interface SysUserMapper {

    Long insert(SysUser SysUser);

    void update(SysUser SysUser);

    SysUser select(SysUser SysUser);

    SysUser selectById(@Param("id") Long id);

    List<SysUser> selectAll(@Param("sort") String sort, @Param("order") String order, @Param("loginName") String loginName, @Param("zhName") String zhName,
                            @Param("email") String email, @Param("phone") String phone, @Param("address") String address,
                            @Param("createTimeStart") Date createTimeStart, @Param("createTimeEnd") Date createTimeEnd,
                            @Param("updateTimeStart") Date updateTimeStart, @Param("updateTimeEnd") Date updateTimeEnd
                            );

    List<SysUser> selectAllInRank(@Param("sort") String sort, @Param("order") String order, @Param("loginName") String loginName, @Param("zhName") String zhName,
                            @Param("email") String email, @Param("phone") String phone, @Param("address") String address,
                            @Param("createTimeStart") Date createTimeStart, @Param("createTimeEnd") Date createTimeEnd,
                            @Param("updateTimeStart") Date updateTimeStart, @Param("updateTimeEnd") Date updateTimeEnd,
                                  @Param("rank") Long rank);

    int selectCountsInRank(@Param("sort") String sort, @Param("order") String order, @Param("loginName") String loginName, @Param("zhName") String zhName,
                           @Param("email") String email, @Param("phone") String phone, @Param("address") String address,
                           @Param("createTimeStart") Date createTimeStart, @Param("createTimeEnd") Date createTimeEnd,
                           @Param("updateTimeStart") Date updateTimeStart, @Param("updateTimeEnd") Date updateTimeEnd,
                           @Param("rank") Long rank);

    int selectCounts();

    boolean isExistLoginNameByName(@Param("loginName") String loginName);

    void deleteById(@Param("id") long id);

    boolean isExistLoginNameExcludeId(@Param("id") long id, @Param("loginName") String loginName);

    SysUser selectUserByLoginName(@Param("loginName") String loginName);

    UserDetailsBean selectInfoByLoginName(String name);

    //删除status==2的记录
    void shrink();
}
