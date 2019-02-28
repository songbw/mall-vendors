package com.software.seller.controller;

import com.software.seller.bean.*;
import com.software.seller.model.SysOrganization;
import com.software.seller.model.SysRole;
import com.software.seller.model.SysUser;
import com.software.seller.service.impl.SysOrganizationServiceImpl;
import com.software.seller.service.impl.SysRoleServiceImpl;
import com.software.seller.service.impl.SysUserServiceImpl;
import com.software.seller.util.*;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;

//import java.text.ParseException;
//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Date;
//import java.util.HashMap;
import java.util.List;


@Api(tags="RoleAPI", description = "用户角色相关", produces = "application/json;charset=UTF-8")
@RestController
@RequestMapping(value = "/role", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

public class RoleController {

    private SysUserServiceImpl sysUserService;
    private SysOrganizationServiceImpl sysOrganizationService;
    private SysRoleServiceImpl sysRoleService;

    private String getPermissions(List<Long> permissionIds) {
        StringBuilder permissions = new StringBuilder(" ");

        if (null != permissionIds && 0 < permissionIds.size()) {
            for (Long permissionId : permissionIds) {
                permissions.append(permissionId);
                permissions.append(",");
            }
            permissions.delete(permissions.length()-1,permissions.length()-1);
        }

        return permissions.toString();

    }

    @Autowired
    public RoleController(      SysUserServiceImpl sysUserService,
                                SysOrganizationServiceImpl sysOrganizationService,
                                SysRoleServiceImpl sysRoleService){

        this.sysUserService = sysUserService;
        this.sysOrganizationService = sysOrganizationService;
        this.sysRoleService = sysRoleService;
    }

    @ApiOperation(value = "增加组织", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/org/insert")
    @PreAuthorize("hasPermission('organization','insert')")
    public ResultMsg insertOrg(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                            @ApiParam(value="data: 名称,全名必须填写,其他的可以空缺",required=true)
                            @RequestBody SysOrganizationListBean data ) {

        data.normalize();
        ResultMsg result = new ResultMsg();
        SysOrganization sysOrganization = new SysOrganization();
        long userId = sysUserService.getUserIdInToken(authentication);
        String name = data.getName();
        String fullName = data.getFullName();
        String description = data.getDescription();
        Integer isFinal = data.getIsFinal();
        Long parentId = data.getParentId();

        if (name.isEmpty() || fullName.isEmpty() || null == parentId) {
            result.setCode(300003);
            result.setMsg("名称,全名不能为空");
            return result;
        }

        if (sysOrganizationService.isExistFullName(fullName) ||
                sysOrganizationService.isExistName(name)) {
            result.setCode(300000);
            result.setMsg("组织已经存在");
            return result;
        }

        sysOrganization.setName(data.getName());
        sysOrganization.setFullName(data.getFullName());
        sysOrganization.setParentId(parentId);

        if (null != description && !description.isEmpty()) {
            sysOrganization.setDescription(data.getDescription());
        }

        if (null != isFinal) {
            if (DataFinal.freeze.getCode() == isFinal) {
                sysOrganization.setIsFinal(DataFinal.freeze.getCode());
            } else {
                sysOrganization.setIsFinal(DataFinal.readWrite.getCode());
            }
        }

        sysOrganization.setCreateTime(new Date());
        sysOrganization.setUpdateTime(new Date());
        if (userId > 0) {
            sysOrganization.setCreateBy(userId);
            sysOrganization.setUpdateBy(userId);
        }

        SysOrganization parent = sysOrganizationService.selectOrganization(parentId);
        Long rank = parent.getRank() + 1L;
        sysOrganization.setRank(rank);
        long id = sysOrganizationService.insertOrganization(sysOrganization, data.getRoleIds());

        result.setCode(200);
        result.setMsg("id: " + id);
        //System.out.println("organization insert success");
        return result;
    }

    @ApiOperation(value = "删除组织", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/org/delete")
    @PreAuthorize("hasPermission('organization','delete')")
    public ResultMsg deleteOrgById(@ApiParam(value="data: ID必须填写,其他的空缺",required=true)
                                @RequestBody SysOrganizationListBean data ) {

        ResultMsg result = new ResultMsg();

        if (null == data.getId()) {
            result.setCode(300000);
            result.setMsg("ID不能为空");
            return result;
        }

        if (sysOrganizationService.deleteOrganization(data.getId())) {
            result.setCode(200);
        } else {
            result.setCode(300000);
            result.setMsg("need try again");
        }
        return result;
    }

    @ApiOperation(value = "更新组织信息", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/org/update")
    @PreAuthorize("hasPermission('organization','update')")
    public ResultMsg updateOrg(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                            @ApiParam(value="data: ID必须填写,其他的可以空缺",required=true)
                            @RequestBody SysOrganizationListBean data ) {

        ResultMsg result = new ResultMsg();
        String name = data.getName();
        String fullName = data.getFullName();
        String description = data.getDescription();
        Long rank = data.getRank();
        Integer isFinal = data.getIsFinal();
        Long parentId = data.getParentId();
        SysOrganization sysOrganization;
        long userId = sysUserService.getUserIdInToken(authentication);
        Long id = data.getId();
        sysOrganization = sysOrganizationService.selectOrganization(id);

        if ( null == sysOrganization ) {
            result.setCode(300003);
            result.setMsg("组织不存在");
            return result;
        }
        if (null != name && !name.isEmpty()) {
            sysOrganization.setName(data.getName());
        }
        if (null != fullName && !fullName.isEmpty()) {
            sysOrganization.setFullName(data.getFullName());
        }
        if (null != description && !description.isEmpty()) {
            sysOrganization.setDescription(data.getDescription());
        }
        if (null != parentId) { sysOrganization.setParentId(parentId);}
        if (null != isFinal) {
            if (DataFinal.freeze.getCode() == isFinal) {
                sysOrganization.setIsFinal(DataFinal.freeze.getCode());
            } else {
                sysOrganization.setIsFinal(DataFinal.readWrite.getCode());
            }
        }
        if (null != rank) {
            sysOrganization.setRank(rank);
        }
        sysOrganization.setUpdateTime(new Date());
        if (userId > 0) {
            sysOrganization.setUpdateBy(userId);
        }

        sysOrganizationService.updateOrganization(sysOrganization, data.getRoleIds());
        result.setCode(200);
        result.setMsg("success");
        //System.out.println("update success");
        return result;
    }

    @ApiOperation(value = "组织列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/org/list")
    @PreAuthorize("hasPermission('organization','list')")
    public ResultObject<PageInfo<SysOrganizationTree>> OrgList(@ApiParam(value="data:pageIndex, pageSize, id",required=true)
                                                    @RequestBody PageRequestBean<SysUserBean> postData) {

        PageInfo<SysOrganizationTree> pageInfo = sysOrganizationService.selectPage(postData.getPageIndex(),postData.getPageSize());

        return new ResultObject<>(200,"success",pageInfo);
    }

    @ApiOperation(value = "子组织列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/org/childrenList")
    @PreAuthorize("hasPermission('organization','list')")
    public ResultObject<PageInfo<SysOrganizationTree>> childrenList(@ApiParam(value="current organization id",required=true)
                                                            @RequestParam long id) {

        List<SysOrganizationTree> list = sysOrganizationService.selectChildrenTreeList(id);
        PageInfo<SysOrganizationTree> pageInfo = new PageInfo<>(list.size(), list.size(), list);

        return new ResultObject<>(200,"success",pageInfo);
    }

    @ApiOperation(value = "查询组织信息", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/org/selectById")
    @PreAuthorize("hasPermission('organization','list')")
    public ResultObject<SysOrganizationTree> listByOrgId(@ApiParam(value="data:pageIndex, pageSize",required=true)
                                                            @RequestParam long id) {

        SysOrganization sysOrganization = sysOrganizationService.selectOrganization(id);
        SysOrganizationTree sysOrganizationTree = new SysOrganizationTree();

        BeanUtils.copyProperties(sysOrganization, sysOrganizationTree);
        return new ResultObject<>(200,"success",sysOrganizationTree);
    }

    @ApiOperation(value = "查询组织分层信息", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/org/tree")
    @PreAuthorize("hasPermission('organization','list')")
    public ResultObject<List<SysOrganizationTree>> orgTree(
            @RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication) {

        SysUser sysUser = null;

        String username = JwtTokenUtil.getUsername(authentication);
        if (null != username && !username.isEmpty()) {
            //System.out.println("=== get username: " + username);
            sysUser = sysUserService.selectByLoginName(username);
        }

        if (null == sysUser) {
            return new ResultObject<>(200,"permission deny", null);
        }

        long userId = sysUser.getId();
        long organizationId = sysUserService.selectOrganizationId(userId);
        if (0 == organizationId) {
            return new ResultObject<>(200,"permission deny", null);
        }

        SysOrganizationTree tree = sysOrganizationService.selectSysOrganizationTree(organizationId);

        return new ResultObject<>(200,"success", tree.getChildren());
    }

    @ApiOperation(value = "查询组织信息列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/org/listAll")
    @PreAuthorize("hasPermission('organization','list')")
    public ResultObject<List<SysOrganizationListBean>> listAll() {

        List<SysOrganizationListBean> sysOrganizationList = sysOrganizationService.selectList();

        return new ResultObject<>(200,"success",sysOrganizationList);
    }

    @ApiOperation(value = "查询组织", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/org/listSpecial")
    @PreAuthorize("hasPermission('organization','list')")
    public ResultObject<PageInfo<SysOrganizationListBean>> orgListSpecial(@ApiParam(value="data:pageIndex, pageSize",required=true)
                                                    @RequestBody PageRequestBean<SysOrganizationListBean> postData) {

        ResultObject<PageInfo<SysOrganizationListBean>> result = new ResultObject<>(200,"success",null);
        if (null == postData || null == postData.getData()) {
            result.setCode(30003);
            result.setMsg("need post data");
            return result;
        }

        String orgName = postData.getData().getName();
        String description = postData.getData().getDescription();
        Long parentId = postData.getData().getParentId();
        List<Long> roles = postData.getData().getRoleIds();
        Integer pageIndex = postData.getPageIndex();
        Integer pageSize = postData.getPageSize();

        PageInfo<SysOrganizationListBean> pageInfo = sysOrganizationService.selectOrgPage(
                pageIndex, pageSize, "id", "ASC",
                orgName, description, parentId, roles);

        result.setData(pageInfo);
        return result;
    }

    @ApiOperation(value = "组织层级列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/org/ranks")
    @PreAuthorize("hasPermission('organization','list')")
    public ResultObject<List<Long>> orgRanks() {
        List<Long> result = sysOrganizationService.selectAllRank();

        return new ResultObject<>(200,"success",result);
    }

    @ApiOperation(value = "查询用户组织层次列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/org/userInTree")
    @PreAuthorize("hasPermission('organization','list')")
    public ResultObject<List<Long>> userInOrgTree(@ApiParam(value="organization Id",required=true)
                                                @RequestParam long id) {

        List<Long> result = new ArrayList<>();
        Long parentId = 0L;

        result.add(0,id);
        SysOrganization o = sysOrganizationService.selectOrganization(id);
        while(o != null && parentId != 1L) {
            parentId = o.getParentId();
            result.add(0,parentId);
            o = sysOrganizationService.selectOrganization(parentId);
        }

        return new ResultObject<>(200,"success",result);
    }

    @ApiOperation(value = "增加角色", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/insert")
    @PreAuthorize("hasPermission('role','insert')")
    public ResultMsg insert(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                            @ApiParam(value="data: 名称必须填写,其他的可以空缺",required=true)
                            @RequestBody UserRolePermissionBean data ) {

        ResultMsg result = new ResultMsg();
        SysRole sysRole = new SysRole();

        long userId = sysUserService.getUserIdInToken(authentication);
        String name = data.getName();

        if (name.isEmpty()) {
            result.setCode(300003);
            result.setMsg("名称不能为空");
            return result;
        }

        if (sysRoleService.isExsitRoleName(name)) {
            result.setCode(300000);
            result.setMsg("角色已经存在");
            return result;
        }

        sysRole.setName(name);
        if (null != data.getDescription()) {
            sysRole.setDescription(data.getDescription());
        }
        sysRole.setStatus(1);
        sysRole.setRank(1L);
        sysRole.setCreateTime(new Date());
        sysRole.setUpdateTime(new Date());
        if (userId > 0) {
            sysRole.setCreateBy(userId);
            sysRole.setUpdateBy(userId);
        }

        sysRoleService.insertRole(sysRole, data.getPermissionGroups());
        result.setCode(200);
        result.setMsg("success");
        //System.out.println("organization insert success");
        return result;
    }

    @ApiOperation(value = "删除角色", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/deleteById")
    @PreAuthorize("hasPermission('role','delete')")
    public ResultMsg deleteById(@ApiParam(value="data: ID必须填写,其他的空缺",required=true)
                                @RequestBody UserRolePermissionBean data ) {

        ResultMsg result = new ResultMsg();

        if (null == data.getId()) {
            result.setCode(300000);
            result.setMsg("ID不能为空");
            return result;
        }

        sysRoleService.deleteRoleById(data.getId());
        result.setCode(200);

        return result;
    }

    @ApiOperation(value = "更新角色信息", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/update")
    @PreAuthorize("hasPermission('role','update')")
    public ResultMsg update(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                            @ApiParam(value="data: ID必须填写,其他的可以空缺",required=true)
                            @RequestBody UserRolePermissionBean data ) {

        ResultMsg result = new ResultMsg();
        if (null == data.getName() || null == data.getId()) {
            result.setCode(300003);
            result.setMsg("Name is absent");
            return result;
        }

        Long id = data.getId();
        String roleName = data.getName();
        SysRoleBean sysRoleBean = sysRoleService.selectById(id);
        if ( null == sysRoleBean || !sysRoleService.isExsitRoleName(roleName)) {
            result.setCode(300003);
            result.setMsg("information mismatch");
            return result;
        }

        SysRole sysRole = new SysRole();
        sysRole.setUpdateTime(new Date());
        sysRole.setId(id);
        sysRole.setName(roleName);
        if (null != data.getDescription()) {
            sysRole.setDescription(data.getDescription());
        }

        long userId = sysUserService.getUserIdInToken(authentication);
        if (userId > 0) {
            sysRole.setUpdateBy(userId);
        }

        sysRoleService.updateRole(sysRole, data.getPermissionGroups());
        result.setCode(200);
        result.setMsg("success");
        //System.out.println("update success");
        return result;
    }

    @ApiOperation(value = "角色列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/list")
    @PreAuthorize("hasPermission('role','list')")
    public ResultObject<PageInfo<SysRoleBean>> list(@ApiParam(value="data:pageIndex, pageSize",required=true)
                                                    @RequestBody PageRequestBean<SysUserBean> postData) {

        PageInfo<SysRoleBean> pageInfo = sysRoleService.selectPage(postData.getPageIndex(),postData.getPageSize());

        return new ResultObject<>(200,"success",pageInfo);
    }

    @ApiOperation(value = "角色列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/listSpecial")
    @PreAuthorize("hasPermission('role','list')")
    public ResultObject<List<UserRolePermissionBean>> listSpecial(@ApiParam(value="data",required=true)
                                                    @RequestBody UserRolePermissionBean postData) {

        List<UserRolePermissionBean> result = sysRoleService.selectSpecial(postData);

        return new ResultObject<>(200,"success", result);
    }

    @ApiOperation(value = "基于当前用户的角色列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/listByUser")
    @PreAuthorize("hasPermission('role','list')")
    public ResultObject<List<SysRoleBean>> listByUser(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication) {

        long userId = sysUserService.getUserIdInToken(authentication);
        List<Long> orgIds = sysOrganizationService.selectChildrenListByUserId(userId);
        //System.out.println("== get org list: " + orgIds);
        List<SysRoleBean> roles = sysRoleService.selectByOrgIds(orgIds);

        return new ResultObject<>(200,"success", roles);
    }

    @ApiOperation(value = "查询角色信息", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/selectById")
    @PreAuthorize("hasPermission('role','list')")
    public ResultObject<SysRoleBean> selectById(@ApiParam(value="data:pageIndex, pageSize",required=true)
                                                            @RequestParam long id) {

        return new ResultObject<>(200,"success",sysRoleService.selectById(id));
    }

}
