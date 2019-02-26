package com.software.seller.service.impl;

//import com.fasterxml.jackson.databind.BeanProperty;
import com.software.seller.bean.SysOrganizationListBean;
//import com.software.seller.mapper.SysUserRoleOrganizationMapper;
import com.software.seller.mapper.SysRoleMapper;
import com.software.seller.mapper.SysUserRoleOrganizationMapper;
import com.software.seller.model.SysRole;
import com.software.seller.model.SysRoleOrganization;
import com.software.seller.model.SysUserRoleOrganization;
import com.software.seller.util.DataFinal;
import com.software.seller.util.DataStatus;
import com.github.pagehelper.PageHelper;
import com.software.seller.mapper.SysOrganizationMapper;
import com.software.seller.mapper.SysRoleOrganizationMapper;
import com.software.seller.util.PageInfo;
import com.software.seller.bean.SysOrganizationTree;
import com.software.seller.model.SysOrganization;
import com.software.seller.service.ISysOrganizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysOrganizationServiceImpl implements ISysOrganizationService {

    private SysOrganizationMapper sysOrganizationMapper;
    private SysRoleOrganizationMapper sysRoleOrganizationMapper;
    private SysUserRoleOrganizationMapper sysUserRoleOrganizationMapper;
    private SysRoleMapper sysRoleMapper;

    @Autowired
    public SysOrganizationServiceImpl(SysOrganizationMapper sysOrganizationMapper,
                                      SysUserRoleOrganizationMapper sysUserRoleOrganizationMapper,
                                      SysRoleMapper sysRoleMapper,
                                      SysRoleOrganizationMapper sysRoleOrganizationMapper) {
        this.sysOrganizationMapper = sysOrganizationMapper;
        this.sysRoleOrganizationMapper = sysRoleOrganizationMapper;
        this.sysUserRoleOrganizationMapper = sysUserRoleOrganizationMapper;
        this.sysRoleMapper = sysRoleMapper;
    }

    private void InsertRoleOrganization(SysOrganization sysOrganization, List<Long> roleIds) {
        if (roleIds.size() > 0) {
            for (Long id : roleIds) {
                SysRoleOrganization sysRoleOrganization = new SysRoleOrganization();

                sysRoleOrganization.setSysRoleId(id);
                sysRoleOrganization.setSysOrganizationId(sysOrganization.getId());
                sysRoleOrganization.setCreateBy(sysOrganization.getUpdateBy());
                sysRoleOrganization.setCreateTime(sysOrganization.getUpdateTime());
                sysRoleOrganization.setUpdateBy(sysOrganization.getUpdateBy());
                sysRoleOrganization.setUpdateTime(sysOrganization.getUpdateTime());
                sysRoleOrganization.setStatus(DataStatus.normal.getCode());
                sysRoleOrganization.setIsFinal(DataFinal.readWrite.getCode());
                sysRoleOrganizationMapper.insert(sysRoleOrganization);
            }
        }
    }

    private List<Long> getRoleNeedAdd(SysOrganization sysOrg, List<Long> roleIds) {
        List<Long> roleIdNew = new ArrayList<>();
        if (roleIds.size() == 0 || null == sysOrg.getId()) {
            return roleIdNew;
        }

        for (Long id : roleIds) {
            sysRoleOrganizationMapper.recoveryRoleOrg(sysOrg.getId(),id,sysOrg.getUpdateBy(),sysOrg.getUpdateTime());
        }
        List<Long> oldRoleId = sysRoleOrganizationMapper.selectRoleIdByOrgId(sysOrg.getId());

        if (oldRoleId.size() > 0) {
            for (Long id : oldRoleId) {
                if (!roleIds.contains(id)) {
                    sysRoleOrganizationMapper.deleteByOrgRoleId(sysOrg.getId(),id);
                }
            }

            for (Long newId : roleIds) {
                if (!oldRoleId.contains(newId)) {
                    roleIdNew.add(newId);
                }
            }
        }
        return roleIdNew;
    }

    @Override
    public Long insertOrganization(SysOrganization sysOrganization, List<Long> roleIds) {

        Long id = sysOrganizationMapper.insert(sysOrganization);
        InsertRoleOrganization(sysOrganization, roleIds);

        return id;

    }

    @Override
    public boolean deleteOrganization(long id) {
        SysOrganizationTree tree = new SysOrganizationTree();
        List<SysOrganizationTree> treeList = selectChildrenTreeList(id);
        tree.setChildren(treeList);
        for (int i = 0; i < treeList.size(); i++) {
            tree.getChildren().set(i, selectSysOrganizationTree(treeList.get(i).getId()));
        }

        List<Long>  orgIdList = tree2List(tree);
        orgIdList.add(id);

        for (Long i : orgIdList) {
            SysOrganization sysOrganization = sysOrganizationMapper.selectById(i);
            sysOrganization.setStatus(DataStatus.delete.getCode());
            sysOrganizationMapper.update(sysOrganization);
            sysRoleOrganizationMapper.deleteByOrgId(id);
        }
        return true;
    }

    @Override
    public void updateOrganization(SysOrganization organization, List<Long> roleIds) {
        sysOrganizationMapper.update(organization);
        List<Long> roleNew = getRoleNeedAdd(organization, roleIds);

        InsertRoleOrganization(organization, roleNew);
    }

    @Override
    public PageInfo<SysOrganizationTree> selectPage(int page, int row) {
        int counts = sysOrganizationMapper.selectCounts();
        int pageSize = 0;
        PageHelper.startPage(page, row);
        List<SysOrganization> list = sysOrganizationMapper.selectAll();

        List<SysOrganizationTree> orgTreeList = new ArrayList<>();
        for (SysOrganization org: list ) {
            SysOrganizationTree tree = new SysOrganizationTree();
            BeanUtils.copyProperties(org, tree);
            orgTreeList.add(tree);
            pageSize++;
        }
        return new PageInfo<>(counts, pageSize, orgTreeList);
    }

    @Override
    public SysOrganizationTree selectSysOrganizationTree(long id) {
        SysOrganizationTree tree = new SysOrganizationTree();
        SysOrganization sysOrganization = sysOrganizationMapper.selectById(id);
        BeanUtils.copyProperties(sysOrganization, tree);
        List<SysOrganizationTree> treeList = selectChildrenTreeList(id);
        tree.setChildren(treeList);
        for (int i = 0; i < treeList.size(); i++) {
            tree.getChildren().set(i, selectSysOrganizationTree(treeList.get(i).getId()));
        }
        return tree;
    }

    private List<Long> tree2List(SysOrganizationTree tree) {
        List<Long> retList = new ArrayList<>();
        if (null == tree || null == tree.getChildren()) {
            return retList;
        }

        List<SysOrganizationTree> children = tree.getChildren();
        for (SysOrganizationTree child : children) {
            retList.add(child.getId());
            List<Long> grandChildren = tree2List(child);
            if (null != grandChildren) {
                retList.addAll(grandChildren);
            }
        }
        return retList;
    }

    @Override
    public List<Long> selectChildrenListByUserId(long userId) {
        List<Long> orgIdList = new ArrayList<>();
        SysUserRoleOrganization userOrg = sysUserRoleOrganizationMapper.selectByUserId(userId);
        if (null == userOrg || null == userOrg.getSysRoleOrganizationId()) {
            return orgIdList;
        }

        SysOrganizationTree tree = new SysOrganizationTree();
        SysOrganization sysOrganization = sysOrganizationMapper.selectById(userOrg.getSysRoleOrganizationId());
        BeanUtils.copyProperties(sysOrganization, tree);
        List<SysOrganizationTree> treeList = selectChildrenTreeList(userOrg.getSysRoleOrganizationId());
        tree.setChildren(treeList);
        for (int i = 0; i < treeList.size(); i++) {
            tree.getChildren().set(i, selectSysOrganizationTree(treeList.get(i).getId()));
        }

        orgIdList = tree2List(tree);

        return orgIdList;
    }

    @Override
    public List<SysOrganizationTree> selectChildrenTreeList(long id) {
        List<SysOrganization> childrenList = sysOrganizationMapper.selectChildren(id);
        List<SysOrganizationTree> childrenTreeList = new ArrayList<>();
        for (SysOrganization s : childrenList) {
            SysOrganizationTree sysOrganizationTree = new SysOrganizationTree();
            BeanUtils.copyProperties(s, sysOrganizationTree);
            childrenTreeList.add(sysOrganizationTree);
        }
        return childrenTreeList;
    }

    @Override
    public List<SysOrganization> selectChildrenByRank(Long parentId, Long rank) {
        return sysOrganizationMapper.selectChildrenByRank(parentId, rank);
    }

    @Override
    public List<Long> selectAllRank() {
        return sysOrganizationMapper.selectAllRank();
    }

    @Override
    public boolean isExistFullName(String fullName) {
        return sysOrganizationMapper.isExistFullName(fullName);
    }

    @Override
    public boolean isExistName(String name) {
        return sysOrganizationMapper.isExistName(name);
    }

    @Override
    public SysOrganization selectOrganization(long id) {
        return sysOrganizationMapper.selectById(id);
    }

    @Override
    public boolean isExistFullNameExcludeId(long id, String fullName) {

        return sysOrganizationMapper.isExistFullNameExcludeId(id, fullName);
    }

    @Override
    public List<SysOrganizationListBean> selectList() {
        List<SysOrganization> organizations =  sysOrganizationMapper.selectAll();
        List<SysOrganizationListBean> list = new ArrayList<>();
        if (organizations.size() > 0) {
            for(SysOrganization org : organizations) {
                SysOrganizationListBean bean = new SysOrganizationListBean();
                BeanUtils.copyProperties(org, bean);
                list.add(bean);
            }
        }
        return list;

    }

    @Override
    public PageInfo<SysOrganizationListBean> selectOrgPage(
            int page, int rows, String sort, String order,
            String name, String description,
            Long parentId, List<Long> roleIds) {

        int counts = sysOrganizationMapper.selectAllSpecial(sort, order, name, name, description, parentId, roleIds).size();
        List<SysOrganizationListBean> sysOrganizationListBean = new ArrayList<>();

        PageHelper.startPage(page, rows);
        List<SysOrganization> organizations = sysOrganizationMapper.selectAllSpecial(sort, order, name, name, description, parentId, roleIds);


        int pageSize = 0;
        if (organizations.size() > 0) {
            for (SysOrganization s : organizations) {
                pageSize ++;
                SysOrganizationListBean retBean = new SysOrganizationListBean();
                BeanUtils.copyProperties(s, retBean);
                List<Long> idList = sysRoleOrganizationMapper.selectRoleIdByOrgId(s.getId());
                if (null != idList && 0 < idList.size()) {
                    retBean.setRoleIds(idList);
                    StringBuilder roleNames = new StringBuilder();
                    for (Long id : idList) {
                        SysRole role = sysRoleMapper.selectById(id);
                        if (null != role && null != role.getName()) {
                            roleNames.append(role.getName());
                            roleNames.append(",");
                        }
                    }
                    retBean.setRoles(roleNames.toString());
                }

                sysOrganizationListBean.add(retBean);
            }
        }

        return new PageInfo<>(counts, pageSize, sysOrganizationListBean);
    }
}


