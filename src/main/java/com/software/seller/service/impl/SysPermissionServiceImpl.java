package com.software.seller.service.impl;

import com.software.seller.mapper.SysPermissionGroupMapper;
import com.software.seller.mapper.SysPermissionMapper;
import com.software.seller.mapper.SysRolePermissionMapper;
import com.software.seller.model.SysRolePermission;
import com.software.seller.util.PageInfo;
import com.software.seller.bean.SysPermissionBean;
import com.software.seller.model.SysPermission;
import com.software.seller.model.SysPermissionGroup;
import com.software.seller.service.ISysPermissionService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class SysPermissionServiceImpl implements ISysPermissionService {
    //private static Logger log = LoggerFactory.getLogger(SysPermissionServiceImpl.class);

    private SysPermissionMapper sysPermissionMapper;
    private SysPermissionGroupMapper sysPermissionGroupMapper;
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    public SysPermissionServiceImpl(
            SysPermissionMapper sysPermissionMapper,
            SysPermissionGroupMapper sysPermissionGroupMapper,
            SysRolePermissionMapper sysRolePermissionMapper) {
        this.sysPermissionMapper = sysPermissionMapper;
        this.sysPermissionGroupMapper = sysPermissionGroupMapper;
        this.sysRolePermissionMapper = sysRolePermissionMapper;
    }


    @Override
    public boolean isExistName(long groupId, String name) {
        return sysPermissionMapper.isExistName(groupId, name);
    }

    @Override
    public boolean isExistCode(long groupId, String code) {
        return sysPermissionMapper.isExistCode(groupId, code);
    }

    @Override
    public long insertPermission(SysPermission sysPermission) {

        return sysPermissionMapper.insert(sysPermission);
    }

    @Override
    public List<Long> selectIdByRoleId(Long roleId) {
        List<Long> idList = new ArrayList<>();

        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.selectByRoleId(roleId);
        if (null != rolePermissions && rolePermissions.size() > 0) {
            for (SysRolePermission s: rolePermissions) {
                if (null != s.getSysPermissionId()) {
                    idList.add(s.getSysPermissionId());
                }
            }
        }
        return idList;
    }

    @Override
    public List<SysPermission> selectAll() {
        return sysPermissionMapper.selectAll();
    }

    @Override
    public SysPermission selectById(long id) {
        return sysPermissionMapper.selectById(id);
    }

    @Override
    public void update(SysPermission sysPermission) {
        sysPermissionMapper.update(sysPermission);
    }

    @Override
    public boolean isExistNameExcludeId(long id, long groupId, String name) {
        return sysPermissionMapper.isExistNameExcludeId(id, groupId, name);
    }

    @Override
    public boolean isExistCodeExcludeId(long id, long groupId, String code) {
        return sysPermissionMapper.isExistCodeExcludeId(id, groupId, code);
    }

    @Override
    public PageInfo<SysPermissionBean> selectPage(int page, int rows, String sort, String order, String name, String description, String code, Long sysPermissionGroupId) {

        //System.out.println("=== permisssion: page : " + page + " size: "+ rows + "  sysPermissionGroupId: " + sysPermissionGroupId);
        int counts = sysPermissionMapper.selectSpecialCounts(sort, order, name, description, code, sysPermissionGroupId);

        PageHelper.startPage(page, rows);
        List<SysPermission> list = sysPermissionMapper.selectSpecial(sort, order, name, description, code, sysPermissionGroupId);
        List<SysPermissionBean> listResult = new ArrayList<>();

        int pageSize = 0;
        for (int i = 0; i < list.size(); i++) {
            SysPermissionBean sysPermissionBean = new SysPermissionBean();
            BeanUtils.copyProperties(list.get(i), sysPermissionBean);
            long groupId = sysPermissionBean.getSysPermissionGroupId();
            SysPermissionGroup sysPermissionGroup = sysPermissionGroupMapper.selectById(groupId);
            //sysPermissionDto.setSysPermissionGroupName(sysPermissionGroup != null ? sysPermissionGroup.getName() : "");
            listResult.add(sysPermissionBean);
            pageSize ++;
        }
        return new PageInfo<>(counts, pageSize, listResult);
    }

    @Override
    public boolean isExistGroupName(String name) {
        return sysPermissionGroupMapper.isExistGroupName(name);
    }

    @Override
    public long insertPermissionGroup(SysPermissionGroup sysPermissionGroup) {

        return sysPermissionGroupMapper.insert(sysPermissionGroup);
    }

    @Override
    public List<SysPermissionGroup> selectGroup() {

        return sysPermissionGroupMapper.selectAll();
    }

    @Override
    public List<SysPermission> selectByGroupId(Long groupId) {
        return sysPermissionMapper.selectByGroupId(groupId);
    }

    @Override
    public boolean deleteById(long id) {
        sysPermissionMapper.deleteById(id);

        SysPermission permission = sysPermissionMapper.selectById(id);
        return (null == permission);

    }

    @Override
    public boolean deleteGroupById(long id) {
        sysPermissionGroupMapper.deleteById(id);

        SysPermissionGroup group = sysPermissionGroupMapper.selectById(id);

        return null == group;
    }

    @Override
    public SysPermissionGroup selectGroupById(long groupId) {
        return sysPermissionGroupMapper.selectById(groupId);
    }

    @Override
    public void updateGroupById(SysPermissionGroup sysPermissionGroup) {
        sysPermissionGroupMapper.update(sysPermissionGroup);
    }

    @Override
    public List<String> selectCodeByUserId(Long userId) {
        List<String> codes = new ArrayList<>();

        List<SysPermission> permissions = sysPermissionMapper.selectByUserId(userId);

        if (null != permissions && 0 < permissions.size()) {
            for (SysPermission permission : permissions) {
                codes.add(permission.getCode());
            }
        }

        return codes;
    }

    @Override
    public List<SysPermission> selectByUserId(Long userId) {
        return sysPermissionMapper.selectByUserId(userId);
    }
}
