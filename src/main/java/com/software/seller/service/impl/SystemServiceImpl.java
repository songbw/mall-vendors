package com.software.seller.service.impl;

import com.github.pagehelper.PageHelper;
import com.software.seller.bean.SysLogBean;
import com.software.seller.model.*;
import com.software.seller.mapper.*;
import com.software.seller.service.ISystemService;
import com.software.seller.util.DataStatus;
import com.software.seller.util.PageInfo;
import com.software.seller.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SystemServiceImpl implements ISystemService {

    private static final Logger log = LoggerFactory.getLogger(SystemServiceImpl.class);

    private SysLoginStatusMapper sysLoginStatusMapper;
    private SysOrganizationMapper sysOrganizationMapper;
    private SysPermissionGroupMapper sysPermissionGroupMapper;
    private SysPermissionMapper sysPermissionMapper;
    private SysRoleMapper sysRoleMapper;
    private SysRolePermissionMapper sysRolePermissionMapper;
    private SysUserMapper sysUserMapper;
    private SysUserRoleOrganizationMapper sysUserRoleOrganizationMapper;
    private SysRoleOrganizationMapper sysRoleOrganizationMapper;
    private SysUserPermissionMapper sysUserPermissionMapper;
    private SysLogMapper sysLogMapper;

    @Autowired
    public SystemServiceImpl(SysLoginStatusMapper sysLoginStatusMapper,
                             SysUserMapper sysUserMapper,
                             SysOrganizationMapper sysOrganizationMapper,
                             SysPermissionGroupMapper sysPermissionGroupMapper,
                             SysPermissionMapper sysPermissionMapper,
                             SysRoleMapper sysRoleMapper,
                             SysUserRoleOrganizationMapper sysUserRoleOrganizationMapper,
                             SysRolePermissionMapper sysRolePermissionMapper,
                             SysRoleOrganizationMapper sysRoleOrganizationMapper,
                             SysUserPermissionMapper sysUserPermissionMapper,
                             SysLogMapper sysLogMapper) {
        this.sysUserMapper = sysUserMapper;
        this.sysUserRoleOrganizationMapper = sysUserRoleOrganizationMapper;
        this.sysLoginStatusMapper = sysLoginStatusMapper;
        this.sysRoleOrganizationMapper = sysRoleOrganizationMapper;
        this.sysLogMapper = sysLogMapper;
        this.sysOrganizationMapper = sysOrganizationMapper;
        this.sysPermissionGroupMapper = sysPermissionGroupMapper;
        this.sysPermissionMapper = sysPermissionMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysRolePermissionMapper = sysRolePermissionMapper;
        this.sysUserPermissionMapper = sysUserPermissionMapper;
    }

    @Override
    public void forceLogout(long userId) {
        /*
        List<SysLoginStatus> list = sysLoginStatusMapper.selectByUserId(userId);
        for (int i = 0; i < list.size(); i++) {
            SysLoginStatus sysLoginStatus = list.get(i);
            sysLoginStatus.setStatus(2);
            sysLoginStatusMapper.update(sysLoginStatus);
        }*/
        SysUser sysUser = sysUserMapper.selectById(userId);
        String username = sysUser.getLoginName();
        if (null != username) {
            StringUtil.deleteToken(username);
        }
        log.debug("force logout userId : {}", userId);
    }

    @Override
    public void clearAuthorizationInfoCacheByUserId(long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser != null) {
            StringUtil.deleteToken(sysUser.getLoginName());
        }
        log.debug("clear authorization info cache userId : {}", userId);
    }

    @Override
    public void clearAuthorizationInfoALL() {
        //Set<Object> keys = redisTemplate.keys(SystemConstant.shiro_cache_prefix_keys);
       // if (keys.size() > 0) {
       //     redisTemplate.opsForValue().getOperations().delete(keys);
       //     log.debug("clear authorization info cache key : {}", keys.toArray());
       // }
    }

    @Override
    public void clearAuthorizationInfoByRoleId(long roleId) {
        log.debug("clear authorization info cache by roleId: {}", roleId);
        List<Long> list = sysRoleOrganizationMapper.selectIdByRoleId(roleId);
        if (list.size() > 0) {
            for (long id : list) {
                List<Long> userIds = sysUserRoleOrganizationMapper.selectByRoleOrganizationId(id);
                if (userIds.size() > 0) {
                    for (Long userId : userIds) {
                        SysUser sysUser = sysUserMapper.selectById(userId);
                        if (sysUser != null) {
                            StringUtil.deleteToken(sysUser.getLoginName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public PageInfo<SysLoginStatus> selectLogStatus(int page, int rows) {
        int counts = sysLoginStatusMapper.selectCounts();
        PageHelper.startPage(page, rows);
        List<SysLoginStatus> sysLoginStatuses = sysLoginStatusMapper.selectAll();
        int pageSize = sysLoginStatuses==null?0:sysLoginStatuses.size();

        return new PageInfo<>(counts, pageSize, sysLoginStatuses);
    }

    @Override
    public PageInfo<SysLogBean> selectLog(int page, int rows, String sort, String order, String method, String url, String param, Date createTimeStart, Date createTimeEnd, String user) {
        int counts = sysLogMapper.selectLog(sort, order, method, url, param, createTimeStart, createTimeEnd, user).size();

        PageHelper.startPage(page, rows);
        List<SysLog> list = sysLogMapper.selectLog(sort, order, method, url, param, createTimeStart, createTimeEnd, user);
        List<SysLogBean> beans = new ArrayList<>();
        if (null != list && 0 < list.size()) {
            for (SysLog s : list) {
                SysLogBean b = new SysLogBean();
                BeanUtils.copyProperties(s,b);
                String createTime = StringUtil.Date2String(s.getCreateTime());
                b.setCreateTimeStart(createTime);
                b.setTimeUsed(s.getDuration().toString()+" ms");
                beans.add(b);
            }
        }

        int pageSize = list==null?0:list.size();
        return new PageInfo<>(counts, pageSize,beans);
    }

    @Override
    public void insertSysControllerLog(SysLog runningLog) {

        sysLogMapper.insert(runningLog);
    }

    @Override
    public void deleteLogById(Long id) {
        sysLogMapper.deleteById(id, DataStatus.delete.getCode());
    }

    @Override
    public void deleteLogByIdList(List<Long> ids) {
        if (null != ids && 0 < ids.size()) {
            sysLogMapper.deleteByIdList(ids, DataStatus.delete.getCode());
        }
    }

    @Override
    public void shrinkTables() {
        this.sysUserMapper.shrink();
        this.sysUserRoleOrganizationMapper.shrink();
        this.sysLoginStatusMapper.shrink();
        this.sysRoleOrganizationMapper.shrink();
        this.sysLogMapper.shrink();
        this.sysOrganizationMapper.shrink();
        this.sysPermissionGroupMapper.shrink();
        this.sysPermissionMapper.shrink();
        this.sysRoleMapper.shrink();
        this.sysRolePermissionMapper.shrink();
        this.sysUserPermissionMapper.shrink();
    }
}
