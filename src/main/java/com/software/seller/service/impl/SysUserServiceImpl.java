package com.software.seller.service.impl;

import com.github.pagehelper.PageHelper;
import com.software.seller.model.*;
import com.software.seller.mapper.*;
import com.software.seller.service.ISysUserService;
import com.software.seller.util.JwtTokenUtil;
import com.software.seller.util.PageInfo;
import com.software.seller.bean.SysUserBean;
//import org.joda.time.DateTime;
import com.software.seller.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;

//import java.io.IOException;
//import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Service(value="SysUserServiceImpl")
@Transactional
public class SysUserServiceImpl implements ISysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    private SysUserMapper sysUserMapper;
    private SysUserRoleOrganizationMapper sysUserRoleOrganizationMapper;
    private SysOrganizationMapper sysOrganizationMapper;
    private SysUserPermissionMapper sysUserPermissionMapper;
    private SysPermissionMapper sysPermissionMapper;
    //@Autowired
    //private SysLoginStatusMapper sysLoginStatusMapper;

    @Autowired
    public SysUserServiceImpl(SysUserMapper sysUserMapper,
                              SysUserRoleOrganizationMapper sysUserRoleOrganizationMapper,
                              SysOrganizationMapper sysOrganizationMapper,
                              SysUserPermissionMapper sysUserPermissionMapper,
                              SysPermissionMapper sysPermissionMapper
                              ) {
        this.sysUserMapper = sysUserMapper;
        this.sysUserRoleOrganizationMapper = sysUserRoleOrganizationMapper;
        this.sysOrganizationMapper = sysOrganizationMapper;
        this.sysUserPermissionMapper = sysUserPermissionMapper;
        this.sysPermissionMapper = sysPermissionMapper;
        //this.sysLoginStatusMapper = sysLoginStatusMapper;
    }
   // @Autowired
   // private RedisTemplate<Object, Object> redisTemplate;

    /*
    private void insertRolesOrganization(String organizationIds, Long userId) {
        if (StringUtils.hasText(organizationIds)) {
            String[] orgIdArray = organizationIds.split(",");
            for (String orgId : orgIdArray) {
                SysUserRoleOrganization userRoleOrganization = new SysUserRoleOrganization();
                userRoleOrganization.setSysUserId(userId);
                userRoleOrganization.setSysRoleOrganizationId(Long.valueOf(orgId));
                userRoleOrganization.setIsFinal(1);
                sysUserRoleOrganizationMapper.insert(userRoleOrganization);
            }
        }
    }

    private void insertPermissions(String permissionIds, Long userId) {
        if (StringUtils.hasText(permissionIds)) {
            String[] permissionIdArray = permissionIds.split(",");
            for (String permissionId : permissionIdArray) {
                SysUserPermission userPermission = new SysUserPermission();
                userPermission.setSysUserId(userId);
                userPermission.setSysPermissionId(Long.valueOf(permissionId));
                userPermission.setIsFinal(1);
                sysUserPermissionMapper.insert(userPermission);
            }
        }
    }
    */

    private void insertRolesOrganization(Long orgId, Long userId) {
        if (null == orgId || null == userId) {
            return;
        }
        if (!sysUserRoleOrganizationMapper.isExistByUserId(orgId,userId)) {
            SysUserRoleOrganization userRoleOrganization = new SysUserRoleOrganization();
            userRoleOrganization.setSysUserId(userId);
            userRoleOrganization.setSysRoleOrganizationId(orgId);
            userRoleOrganization.setCreateTime(new Date());
            userRoleOrganization.setUpdateTime(new Date());
            userRoleOrganization.setCreateBy(userId);
            userRoleOrganization.setUpdateBy(userId);
            userRoleOrganization.setIsFinal(1);

            sysUserRoleOrganizationMapper.insert(userRoleOrganization);
            log.debug("insert role Organization: {} for userId: {}  ",orgId ,userId);
        }
    }

    private void insertPermissions(List<Long> permissionIds, Long userId) {

        if (null == permissionIds || 0 == userId) {
            return;
        }

        for (Long permissionId : permissionIds) {
            if (!sysUserPermissionMapper.isExistByUserId(userId, permissionId)) {
                SysUserPermission userPermission = new SysUserPermission();
                userPermission.setSysUserId(userId);
                userPermission.setSysPermissionId(permissionId);
                userPermission.setIsFinal(1);
                userPermission.setCreateTime(new Date());
                userPermission.setUpdateTime(new Date());
                userPermission.setCreateBy(userId);
                userPermission.setUpdateBy(userId);
                sysUserPermissionMapper.insert(userPermission);
                log.debug("insert permission: {} for userId: {}  ",permissionId ,userId);
            }
        }
    }

    @Override
    public long insertUser(SysUser user, List<Long> permissionIds, Long roleOrganizationId) {
        sysUserMapper.insert(user);

        insertRolesOrganization(roleOrganizationId, user.getId());
        insertPermissions(permissionIds, user.getId());

        return user.getId();
    }

    @Override
    public boolean isExistLoginName(String loginName) {
        //System.out.println("isExistLoginName: loginName= "+loginName + " mapper: " + sysUserMapper);
        return sysUserMapper.isExistLoginNameByName(loginName);
    }

    @Override
    public void deleteById(long id) {
        sysUserMapper.deleteById(id);
        sysUserPermissionMapper.deleteByUserId(id);
        sysUserRoleOrganizationMapper.deleteUserId(id);
    }

    @Override
    public SysUser selectById(long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public boolean isExistLoginNameExcludeId(long id, String loginName) {
        return sysUserMapper.isExistLoginNameExcludeId(id, loginName);
    }

    @Override
    public void updateUser(SysUser user, List<Long> permissionIds, Long roleOrganizationId) {
        if (null == user) {
            return;
        }
        sysUserMapper.update(user);
        //System.out.println("updated user for: " + user.getId());
        if (null != permissionIds && permissionIds.size() > 0) {
            sysUserPermissionMapper.deleteByUserId(user.getId());
            insertPermissions(permissionIds, user.getId());
        }
        if (null != roleOrganizationId) {
            sysUserRoleOrganizationMapper.deleteUserId(user.getId());
            insertRolesOrganization(roleOrganizationId, user.getId());
        }

    }

    @Override
    public long selectOrganizationId(long userId) {
        SysUserRoleOrganization userRoleOrganization = sysUserRoleOrganizationMapper.selectByUserId(userId);
        if (null != userRoleOrganization) {
            return userRoleOrganization.getSysRoleOrganizationId();
        }
        return 0;
    }

    @Override
    public long selectOrganizationRank(long userId) {
        SysUserRoleOrganization userRoleOrganization = sysUserRoleOrganizationMapper.selectByUserId(userId);
        if (null != userRoleOrganization) {
            long orgId = userRoleOrganization.getSysRoleOrganizationId();
            SysOrganization org = sysOrganizationMapper.selectById(orgId);
            if (null != org) {
                return org.getRank();
            }
        }
        return 9999;
    }

    @Override
    public PageInfo<SysUserBean> selectPage(int page, int rows, String sort, String order, String loginName, String zhName, String email, String phone, String address, Date createTimeStart, Date createTimeEnd, Date updateTimeStart, Date updateTimeEnd, Long rank) {
        /*System.out.println("page = [" + page + "], rows = [" + rows + "], sort = [" + sort + "], order = [" + order + "], loginName = [" + loginName + "], zhName = [" + zhName + "], email = [" + email + "], phone = [" + phone + "]," +
                " address = [" + address + "]" + " createTimeStart = " + createTimeStart + "]" +
                " createTimeEnd = " + createTimeEnd + "]" +
                " updateTimeStart = " + updateTimeStart + "]" +
                " updateTimeEnd = " + updateTimeEnd + "]" );*/
        PageHelper.startPage(page, rows);
        List<SysUser> sysUsers = sysUserMapper.selectAllInRank(sort, order, loginName, zhName, email, phone, address, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd, rank);
        List<SysUserBean> sysUserBeans = new ArrayList<>();
        int counts = sysUserMapper.selectCountsInRank(sort, order, loginName, zhName, email, phone, address, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd, rank);
        int pageSize = 0;
        for (SysUser user : sysUsers) {
            //System.out.println("get : " + user);
            pageSize++;
            SysUserBean userBean = new SysUserBean();
            BeanUtils.copyProperties(user, userBean);
            String createTime = StringUtil.Date2String(user.getCreateTime());
            String updateTime = StringUtil.Date2String(user.getUpdateTime());

            userBean.setCreateTime(createTime);
            userBean.setUpdateTime(updateTime);
            String createBy= "";
            String updateBy ="";
            if (null != user.getCreateBy()) {
                if (null != sysUserMapper.selectById(user.getCreateBy())) {
                    createBy = sysUserMapper.selectById(user.getCreateBy()).getLoginName();
                }
            }
            userBean.setCreateBy(createBy);
            if (null != user.getUpdateBy()) {
                if (null != sysUserMapper.selectById(user.getUpdateBy())) {
                    updateBy = sysUserMapper.selectById(user.getUpdateBy()).getLoginName();
                }
            }
            userBean.setCreateBy(updateBy);

            List<SysPermission> permissions = sysPermissionMapper.selectByUserId(user.getId());
            List<Long> permissionIds = new ArrayList<>();
            for(SysPermission permission : permissions) {
                permissionIds.add(permission.getId());
            }
            userBean.setPermissionIds(permissionIds);

            SysUserRoleOrganization userRoleOrganization = sysUserRoleOrganizationMapper.selectByUserId(user.getId());
            //System.out.println("select org by userId: " + user.getId() + " : get: " + userRoleOrganization);
            if (null != userRoleOrganization) {
                userBean.setOrganizationId(userRoleOrganization.getSysRoleOrganizationId());
            }
            userBean.setPassword("");
            sysUserBeans.add(userBean);
        }

        return new PageInfo<>(counts, pageSize,sysUserBeans);
    }

    @Override
    public SysUser selectByLoginName(String loginName) {
        return sysUserMapper.selectUserByLoginName(loginName);
    }

    @Override
    public void updateUserPermission(long id, List<Long> permissionIds) {
        if (permissionIds.size() > 0) {
            sysUserPermissionMapper.deleteByUserId(id);
            insertPermissions(permissionIds, id);
        }
    }

    @Override
    public void updateUserRoleOrganization(long id, Long roleOrganizationId) {
        if (null != roleOrganizationId) {
            sysUserRoleOrganizationMapper.deleteUserId(id);
            insertRolesOrganization(roleOrganizationId, id);
        }
    }

    @Override
    public long getUserIdInToken(String authentication) {
        long id = 0L;
        String username = JwtTokenUtil.getUsername(authentication);
        if (null != username && !username.isEmpty()) {
            //System.out.println("=== get username: " + username);
            SysUser sysUser = sysUserMapper.selectUserByLoginName(username);
            if (null != sysUser) {
                id = sysUser.getId();
            }
        }
        return id;
    }
/*
    @Override
    public LoginBean login(SysUser user, Serializable id, int platform) {
        log.debug("sessionId is:{}", id.toString());

        LoginBean loginInfo = new LoginBean();

        BeanUtils.copyProperties(user, loginInfo);
        List<SysUserPermission> userPermissions = sysUserPermissionMapper.selectByUserId(user.getId());
        List<SysPermission> permissions = new ArrayList<>();
        for (SysUserPermission userPermission : userPermissions) {
            SysPermission sysPermission = sysPermissionMapper.selectById(userPermission.getSysPermissionId());
            permissions.add(sysPermission);
        }
        List<SysUserRoleOrganization> userRoleOrganizations = sysUserRoleOrganizationMapper.selectByUserId(user.getId());
        loginInfo.setRoles(userRoleOrganizations);

        SysLoginStatus newLoginStatus = new SysLoginStatus();
        newLoginStatus.setSysUserId(user.getId());
        newLoginStatus.setSysUserId(user.getId());
        //newLoginStatus.setSysUserZhName(user.getZhName());
        //newLoginStatus.setSysUserLoginName(user.getLoginName());
        newLoginStatus.setSessionId(id.toString());
        newLoginStatus.setSessionExpires(new DateTime().plusDays(30).toDate());
        newLoginStatus.setPlatform(platform);

        SysLoginStatus oldLoginStatus = sysLoginStatusMapper.selectByUserIdAndPlatform(user.getId(), platform);
        if (oldLoginStatus != null) {
            if (!oldLoginStatus.getSessionId().equals(id.toString())) {
                // redisTemplate.opsForValue().getOperations().delete(oldLoginStatus.getSessionId());
            }
            oldLoginStatus.setStatus(2);
            sysLoginStatusMapper.update(oldLoginStatus);
            newLoginStatus.setLastLoginTime(oldLoginStatus.getCreateTime());
        }
        sysLoginStatusMapper.insert(newLoginStatus);

        return loginInfo;
    }
*/
}
