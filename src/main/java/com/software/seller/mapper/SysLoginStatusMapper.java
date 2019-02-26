package com.software.seller.mapper;

import com.software.seller.model.SysLoginStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "SysLoginStatusMapper")
public interface SysLoginStatusMapper {
    //新增
    public Long insert(SysLoginStatus SysLoginStatus);

    //更新
    public void update(SysLoginStatus SysLoginStatus);

    //通过对象进行查询
    public SysLoginStatus select(SysLoginStatus SysLoginStatus);

    //通过id进行查询
    public SysLoginStatus selectById(@Param("id") Long id);

    //查询全部
    public List<SysLoginStatus> selectAll();

    //查询数量
    public int selectCounts();

    SysLoginStatus selectByUserIdAndPlatform(@Param("userId") Long userId, @Param("platform") int platform);

    List<SysLoginStatus> selectByUserId(@Param("userId") long userId);

    //删除status==2的记录
    void shrink();
}
