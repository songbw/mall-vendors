package com.software.seller.service.impl;

import com.software.seller.mapper.SysOrganizationMapper;
import com.software.seller.mapper.SysRoleMapper;
import com.software.seller.mapper.SysRoleOrganizationMapper;
import com.software.seller.util.DataStatus;
import com.software.seller.util.PageInfo;
import com.software.seller.bean.SysRoleOrganizationTree;
import com.software.seller.model.SysOrganization;
import com.software.seller.model.SysRole;
import com.software.seller.model.SysRoleOrganization;
import com.software.seller.service.ISysRoleOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysRoleOrganizationServiceImpl implements ISysRoleOrganizationService {
    private static Logger log = LoggerFactory.getLogger(SysRoleOrganizationServiceImpl.class);

    private SysRoleOrganizationMapper sysRoleOrganizationMapper;
    private SysRoleMapper sysRoleMapper;
    private SysOrganizationMapper sysOrganizationMapper;

    @Autowired
    public SysRoleOrganizationServiceImpl(SysRoleOrganizationMapper sysRoleOrganizationMapper,
                                          SysRoleMapper sysRoleMapper,
                                          SysOrganizationMapper sysOrganizationMapper) {

        this.sysRoleOrganizationMapper = sysRoleOrganizationMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysOrganizationMapper = sysOrganizationMapper;
    }

    @Override
    public boolean isExistName(String name, long parentId) {
        return sysRoleOrganizationMapper.isExistName(name, parentId);
    }

    @Override
    public long insertRoleOrganization(SysRoleOrganization roleOrganization) {
        return sysRoleOrganizationMapper.insert(roleOrganization);
    }

    @Override
    public boolean isExistNameExcludeId(long id, String name, long parentId) {
        return sysRoleOrganizationMapper.isExistNameExcludeId(id, name, parentId);
    }

    @Override
    public void updateRoleOrganization(SysRoleOrganization roleOrganization) {
        sysRoleOrganizationMapper.update(roleOrganization);
    }

    @Override
    public SysRoleOrganization selectRoleOrganizationById(long id) {

        return sysRoleOrganizationMapper.selectById(id);
    }

    @Override
    public PageInfo<SysRoleOrganizationTree> selectPage(int page, int rows, long id) {
        int counts = sysRoleOrganizationMapper.selectCounts();
        SysRoleOrganizationTree tree = selectSysRoleOrganizationTree(id);
        List<SysRoleOrganizationTree> list = new ArrayList<>();
        list.add(tree);
        return new PageInfo<>(counts, 1, list);
    }

    //查询职位树形结构
    @Override
    public SysRoleOrganizationTree selectSysRoleOrganizationTree(long id) {
        SysRoleOrganizationTree tree = new SysRoleOrganizationTree();
        SysRoleOrganization roleOrganization = sysRoleOrganizationMapper.selectById(id);
        log.debug("roleOrganization :{}", roleOrganization);
        BeanUtils.copyProperties(roleOrganization, tree);

        if (null == roleOrganization.getSysRoleId()) {
            return tree;
        }
        SysRole role = sysRoleMapper.selectById(roleOrganization.getSysRoleId());
        //log.debug("role :{}", role);
        if (null != role) {
            tree.setSysRoleName(role.getName());
        }

        if (null == roleOrganization.getSysOrganizationId()) {
            return tree;
        }
        SysOrganization organization = sysOrganizationMapper.selectById(roleOrganization.getSysOrganizationId());
        if (organization != null && null != organization.getName()) {
            tree.setSysOrganizationName(organization.getName());
        }
        List<SysRoleOrganizationTree> childrenList = selectSysRoleOrganizationTreeChildrenList(id);
        tree.setChildren(childrenList);
        for (int i = 0; i < childrenList.size(); i++) {
            tree.getChildren().set(i, selectSysRoleOrganizationTree(childrenList.get(i).getId()));
        }
        return tree;
    }

    //查询子目录
    @Override
    public List<SysRoleOrganizationTree> selectSysRoleOrganizationTreeChildrenList(long id) {
        List<SysRoleOrganization> childrenList = sysRoleOrganizationMapper.selectChildren(id);
        List<SysRoleOrganizationTree> childrenTreeList = new ArrayList<>();
        for (SysRoleOrganization s : childrenList) {
            SysRoleOrganizationTree sysOrganizationTree = new SysRoleOrganizationTree();
            BeanUtils.copyProperties(s, sysOrganizationTree);
            childrenTreeList.add(sysOrganizationTree);
        }
        return childrenTreeList;
    }

    @Override
    public List<Long> selectOrgIdByRoleId(Long roleId) {

        return sysRoleOrganizationMapper.selectOrgIdByRoleId(roleId);
    }

    @Override
    public void deleteById(Long id) {
        SysRoleOrganization sysRoleOrganization = sysRoleOrganizationMapper.selectById(id);
        sysRoleOrganization.setStatus(DataStatus.delete.getCode());
        sysRoleOrganizationMapper.update(sysRoleOrganization);
    }
}
