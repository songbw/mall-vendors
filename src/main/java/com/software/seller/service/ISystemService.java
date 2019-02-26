package com.software.seller.service;

import com.software.seller.model.SysLog;
import com.software.seller.model.SysLoginStatus;
import com.software.seller.bean.SysLogBean;
import com.software.seller.util.DataStatus;
import com.software.seller.util.PageInfo;

import java.util.Date;
import java.util.List;

public interface ISystemService {
    void forceLogout(long userId);

    void clearAuthorizationInfoCacheByUserId(long userId);

    void clearAuthorizationInfoALL();

    void clearAuthorizationInfoByRoleId(long roleId);

    PageInfo<SysLoginStatus> selectLogStatus(int page, int rows);

    PageInfo<SysLogBean> selectLog(int page, int rows, String s, String order, String method, String url, String param, Date createTimeStart, Date createTimeEnd, String user);

    void insertSysControllerLog(SysLog runningLog);

    void deleteLogById(Long id);

    void deleteLogByIdList(List<Long> ids);
    //删除status==2的记录
    void shrinkTables();

}
