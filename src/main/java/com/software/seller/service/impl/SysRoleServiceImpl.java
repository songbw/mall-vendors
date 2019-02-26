package com.software.seller.service.impl;

import com.github.pagehelper.PageHelper;
import com.software.seller.bean.UserRolePermissionBean;
import com.software.seller.mapper.*;
import com.software.seller.model.*;
import com.software.seller.util.PageInfo;
import com.software.seller.bean.SysRoleBean;
import com.software.seller.service.ISysRoleService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import com.software.seller.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysRoleServiceImpl implements ISysRoleService {
    //private static Logger log = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    private SysRoleMapper sysRoleMapper;

    private SysRolePermissionMapper sysRolePermissionMapper;

    private SysRoleOrganizationMapper sysRoleOrganizationMapper;

    private SysPermissionGroupMapper sysPermissionGroupMapper;

    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper,
                              SysRolePermissionMapper sysRolePermissionMapper,
                              SysPermissionGroupMapper sysPermissionGroupMapper,
                              SysPermissionMapper sysPermissionMapper,
                              SysRoleOrganizationMapper sysRoleOrganizationMapper) {
        this.sysRoleMapper = sysRoleMapper;
        this.sysRolePermissionMapper = sysRolePermissionMapper;
        this.sysRoleOrganizationMapper = sysRoleOrganizationMapper;
        this.sysPermissionGroupMapper = sysPermissionGroupMapper;
        this.sysPermissionMapper = sysPermissionMapper;
    }

    private List<Long> getPermissionNeedAdd(SysRole sysRole, List<Long> permissionIds) {
        List<Long> permissionIdNew = new ArrayList<>();
        if (permissionIds.size() == 0 || null == sysRole.getId()) {
            return permissionIdNew;
        }

        for (Long id : permissionIds) {
            sysRolePermissionMapper.recoveryRolePermission(sysRole.getId(), id, sysRole.getUpdateBy(), sysRole.getUpdateTime());
        }
        List<SysRolePermission> oldRolePermission = sysRolePermissionMapper.selectByRoleId(sysRole.getId());
        List<Long> permissionIdOld = new ArrayList<>();

        if (oldRolePermission.size() > 0) {
            for(SysRolePermission s : oldRolePermission) {
                permissionIdOld.add(s.getSysPermissionId());
            }

            for (Long id : permissionIdOld) {
                if (!permissionIds.contains(id)) {
                    sysRolePermissionMapper.deleteByRolePermissionId(sysRole.getId(),id);
                }
            }

            for (Long newId : permissionIds) {
                if (! permissionIdOld.contains(newId)) {
                    permissionIdNew.add(newId);
                }
            }
        }
        return permissionIdNew;
    }

    private void InsertRolePermission(SysRole sysRole,List<Long> permissionIds) {
        if (permissionIds.size() > 0) {
            for (Long id : permissionIds) {
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setSysRoleId(sysRole.getId());
                sysRolePermission.setSysPermissionId(id);
                sysRolePermission.setCreateBy(sysRole.getUpdateBy());
                sysRolePermission.setCreateTime(sysRole.getUpdateTime());
                sysRolePermission.setUpdateBy(sysRole.getUpdateBy());
                sysRolePermission.setUpdateTime(sysRole.getUpdateTime());
                sysRolePermissionMapper.insert(sysRolePermission);
            }
        }
    }

    private void updateRolePermission(SysRole sysRole, List<Long> permissionIds) {
        if (permissionIds.size() == 0) {
            return;
        }
        System.out.println("===updateRole: permission: " + permissionIds);
        List<Long> permissionIdNew = getPermissionNeedAdd(sysRole, permissionIds);

        InsertRolePermission(sysRole, permissionIdNew);
    }

    @Override
    public boolean isExsitRoleName(String name) {
        return sysRoleMapper.isExsitName(name);
    }

    @Override
    public void insertRole(SysRole sysRole, List<Long> permissionIds) {
        sysRoleMapper.insert(sysRole);
        InsertRolePermission(sysRole,permissionIds);
        return;
    }

    @Override
    public boolean isExsitRoleNameExcludeId(long id, String name) {
        return sysRoleMapper.isExsitNameExcludeId(id, name);
    }

    @Override
    public SysRoleBean selectById(long id) {

        SysRole sysRole = sysRoleMapper.selectById(id);

        if ( null == sysRole) {
            return null;
        }

        SysRoleBean sysRoleBean = new SysRoleBean();
        BeanUtils.copyProperties(sysRole, sysRoleBean);
        List<SysRolePermission> sysRolePermissionList = sysRolePermissionMapper.selectByRoleId(sysRole.getId());

        List<Long> sysPermissionIdList = new ArrayList<>();
        for (SysRolePermission sysRolePermission : sysRolePermissionList) {
            sysPermissionIdList.add(sysRolePermission.getSysPermissionId());
        }
        sysRoleBean.setSysPermissions(sysPermissionIdList);

        sysRoleBean.setOrganizationId(sysRoleOrganizationMapper.selectOrgIdByRoleId(sysRole.getId()));

        return sysRoleBean;
    }

    @Override
    public PageInfo<SysRoleBean> selectPage(int page, int row) {
        int counts = sysRoleMapper.selectCounts();
        PageHelper.startPage(page,row);
        List<SysRole> sysRoles = sysRoleMapper.selectAll();

        List<SysRoleBean> sysRoleBeanList = new ArrayList<>();
        int pageSize = 0;
        for (SysRole sysRole : sysRoles) {
            pageSize ++;
            SysRoleBean sysRoleBean = new SysRoleBean();
            BeanUtils.copyProperties(sysRole, sysRoleBean);
            List<SysRolePermission> sysRolePermissionList = sysRolePermissionMapper.selectByRoleId(sysRole.getId());

            List<Long> sysPermissionIdList = new ArrayList<>();
            for (SysRolePermission sysRolePermission : sysRolePermissionList) {
                sysPermissionIdList.add(sysRolePermission.getSysPermissionId());
            }
            sysRoleBean.setSysPermissions(sysPermissionIdList);

            sysRoleBean.setOrganizationId(sysRoleOrganizationMapper.selectOrgIdByRoleId(sysRole.getId()));

            sysRoleBeanList.add(sysRoleBean);
        }

        return new PageInfo<>(counts, pageSize, sysRoleBeanList);
    }

    private List<String> completePermissionCode(List<Long> groupIds, List<String> codes) {
        ArrayList<String> permissionCodes = new ArrayList<>();
        List<String> groupCodes = new ArrayList<>();

        if (null != groupIds && groupIds.size() > 0) {
            for (Long groupId : groupIds) {
                SysPermissionGroup s = sysPermissionGroupMapper.selectById(groupId);
                if (null != s) { groupCodes.add(s.getCode()); }
            }
        } else { //unconditional select all permission groups
            List<SysPermissionGroup> sl = sysPermissionGroupMapper.selectAll();
            if (null != sl && sl.size() > 0) {
                for (SysPermissionGroup group : sl) {
                    groupCodes.add(group.getCode());
                }
            }
        }

        for (String s : codes) {
            if (groupCodes.size() > 0) {
                for (String gCode : groupCodes) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(gCode);
                    sb.append(":");
                    sb.append(s);
                    permissionCodes.add(sb.toString());
                }
            }
        }
        return permissionCodes;
    }

    @Override
    public List<UserRolePermissionBean> selectSpecial(UserRolePermissionBean bean) {
        List<UserRolePermissionBean> rolePermissionBeans = new ArrayList<>();
        List<String> permissionCodes = new ArrayList<>();

        //System.out.println("=== groups: " + bean.getPermissionGroups());
        //System.out.println("=== codes: " + bean.getPermissionCodes());
        if (null != bean.getPermissionCodes() && bean.getPermissionCodes().size() > 0) {
            permissionCodes = completePermissionCode( bean.getPermissionGroups(),bean.getPermissionCodes());
        }

        //System.out.println("=== complete codes: " + permissionCodes);
        List<SysRole> roles = sysRoleMapper.selectAllSpecial(bean.getName(),bean.getDescription(),permissionCodes,bean.getPermissionGroups());
        if (null != roles && roles.size() > 0) {
            for(SysRole r : roles) {
                UserRolePermissionBean item = new UserRolePermissionBean();
                BeanUtils.copyProperties(r,item);
                List<SysPermission> t = sysPermissionMapper.selectByRoleId(r.getId());
                List<Long> groupId = new ArrayList<>();
                List<String> retCode = new ArrayList<>();
                if (null != t && t.size() > 0) {
                    for (SysPermission s : t) {
                        if (null != s.getDescription()) {
                            retCode.add(s.getDescription());
                        }
                        if (null != s.getSysPermissionGroupId()) {
                            if (!groupId.contains(s.getSysPermissionGroupId())) {
                                groupId.add(s.getSysPermissionGroupId());
                            }
                        }
                    }
                }
                item.setPermissionGroups(groupId);
                item.setPermissionCodes(retCode);
                rolePermissionBeans.add(item);
            }
        }

        return rolePermissionBeans;
    }

    @Override
    public void deleteRole(SysRole sysRole) {
        sysRoleMapper.update(sysRole);
        sysRolePermissionMapper.deleteByRoleId(sysRole.getId());
    }

    @Override
    public void deleteRoleById(Long id) {
        sysRoleMapper.deleteById(id);
        sysRolePermissionMapper.deleteByRoleId(id);
    }

    @Override
    public void updateRole(SysRole sysRole, List<Long> permissionIds) {
        //System.out.println("===updateRole: " + sysRole);
        sysRoleMapper.update(sysRole);
        updateRolePermission(sysRole, permissionIds);
    }

    @Override
    public List<String> selectRoleByUserId(long userId) {
        return sysRoleMapper.selectNameByUserId(userId);
    }

    @Override
    public List<SysRoleBean> selectByOrgIds(List<Long> orgIds) {
        List<SysRoleBean> beans = new ArrayList<>();
        if (null ==orgIds ) {
            return beans;
        }

        List<SysRole> roles = sysRoleMapper.selectByOrgId(orgIds);
        if (null == roles || roles.size() ==0) {
            return beans;
        }

        for(SysRole role: roles) {
            SysRoleBean bean = new SysRoleBean();
            BeanUtils.copyProperties(role, bean);
            beans.add(bean);
        }
        return beans;
    }
}
