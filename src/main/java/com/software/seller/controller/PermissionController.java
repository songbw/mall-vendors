package com.software.seller.controller;

import com.software.seller.bean.PageRequestBean;
import com.software.seller.bean.SysPermissionBean;
import com.software.seller.bean.SysPermissionGroupBean;
import com.software.seller.mapper.SysPermissionGroupMapper;
import com.software.seller.model.SysPermissionGroup;
import com.software.seller.service.impl.SysPermissionServiceImpl;
import com.software.seller.model.SysPermission;
import com.software.seller.model.SysUser;
import com.software.seller.service.impl.SysUserServiceImpl;
import com.software.seller.util.*;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;

//import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
//import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

@Api(tags="PermissionAPI", description = "用户权限相关", produces = "application/json;charset=UTF-8")
@RestController
@RequestMapping(value = "/permission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PermissionController {

    private SysUserServiceImpl sysUserService;
    private SysPermissionServiceImpl sysPermissionService;
    private SysPermissionGroupMapper sysPermissionGroupMapper;

    @Autowired
    public PermissionController(
            SysPermissionServiceImpl sysPermissionService,
            SysUserServiceImpl sysUserService,
            SysPermissionGroupMapper sysPermissionGroupMapper) {
        this.sysPermissionService = sysPermissionService;
        this.sysUserService = sysUserService;
        this.sysPermissionGroupMapper = sysPermissionGroupMapper;
    }

    @ApiOperation(value = "增加权限", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/insert")
    @PreAuthorize("hasPermission('permission','insert')")
    public ResultMsg insert(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                            @ApiParam(value="data: 权限名,编码,祖ID必须填写,其他的可以空缺",required=true)
                              @RequestBody SysPermissionBean data ) {

        data.normalize();
        ResultMsg result = new ResultMsg();
        SysPermission sysPermission = new SysPermission();

        String username = null;
        long userId = sysUserService.getUserIdInToken(authentication);
        String name = data.getName();
        String code = data.getCode();
        String description = data.getDescription();
        Long permissionGroupId = data.getSysPermissionGroupId();

        if (name.isEmpty() || code.isEmpty()) {
            result.setCode(300003);
            result.setMsg("权限名与编码不能为空");
            return result;
        }

        SysPermissionGroup permissionGroup = sysPermissionGroupMapper.selectById(permissionGroupId);
        if (null != permissionGroup) {
            String finalCode = permissionGroup.getCode() + ":" + code;
            code = finalCode;
        }
        sysPermission.setCode(code);

        if (sysPermissionService.isExistCode(permissionGroupId, code) ||
                sysPermissionService.isExistCode(permissionGroupId, code)) {
            result.setCode(300000);
            result.setMsg("权限已经存在");
            return result;
        }

        sysPermission.setName(name);
        sysPermission.setDescription(description);
        sysPermission.setSysPermissionGroupId(permissionGroupId);
        sysPermission.setIsFinal(data.getIsFinal());
        sysPermission.setStatus(1);
        sysPermission.setRank(1L);
        sysPermission.setCreateTime(new Date());
        sysPermission.setUpdateTime(new Date());
        if (userId > 0) {
            sysPermission.setCreateBy(userId);
            sysPermission.setUpdateBy(userId);
        }

        long id = sysPermissionService.insertPermission(sysPermission);
        result.setCode(200);
        result.setMsg("id:"+id);
        System.out.println("permission insert success");
        return result;
    }

    @ApiOperation(value = "删除权限", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/delete")
    @PreAuthorize("hasPermission('permission','delete')")
    public ResultMsg deleteById(@ApiParam(value="data: ID必须填写,其他的空缺",required=true)
                            @RequestBody SysPermissionBean data ) {

        data.normalize();
        ResultMsg result = new ResultMsg();

        if (sysPermissionService.deleteById(data.getId())) {
            result.setCode(200);
        } else {
            result.setCode(300000);
            result.setMsg("need try again");
        }
        return result;
    }

    @ApiOperation(value = "更新权限信息", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/update")
    @PreAuthorize("hasPermission('permission','update')")
    public ResultMsg update(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                            @ApiParam(value="data: ID必须填写,其他的可以空缺",required=true)
                            @RequestBody SysPermissionBean data ) {

        data.normalize();
        ResultMsg result = new ResultMsg();
        SysPermission sysPermission;
        SysPermissionGroup permissionGroup = null;
        Long permissionGroupId = data.getSysPermissionGroupId();
        String username = null;
        String code = data.getCode();
        long userId = sysUserService.getUserIdInToken(authentication);
        if (null == data.getId()) {
            result.setCode(300003);
            result.setMsg("Id is missed!");
            return result;
        }
        long id = data.getId();

        sysPermission = sysPermissionService.selectById(id);
        if ( null == sysPermission ) {
            result.setCode(300003);
            result.setMsg("权限不存在");
            return result;
        }

        if (null != code ) {
            if (null != permissionGroupId) {
                permissionGroup = sysPermissionGroupMapper.selectById(permissionGroupId);
            }
            if (null != permissionGroup) {
                String finalCode = permissionGroup.getCode() + ":" + code;
                sysPermission.setCode(finalCode);
            } else {
                sysPermission.setCode(code);
            }
        }

        sysPermission.setName(data.getName());
        sysPermission.setDescription(data.getDescription());
        //sysPermission.setIsFinal(data.getIsFinal());
        //sysPermission.setRank(data.getRank());
        sysPermission.setUpdateTime(new Date());

        if (userId > 0) {
            sysPermission.setUpdateBy(userId);
        }

        sysPermissionService.update(sysPermission);
        result.setCode(200);
        result.setMsg("success");
        System.out.println("update success");
        return result;
    }

    @ApiOperation(value = "权限列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/listAll")
    @PreAuthorize("hasPermission('permission','list')")
    public ResultObject<ArrayList<SysPermissionBean>> getPermissionsAll() {
        ArrayList<SysPermissionBean> permissions = new ArrayList<>();
        List<SysPermission> permissionList = sysPermissionService.selectAll();

        if (null != permissionList && permissionList.size() > 0) {
            for (SysPermission permission : permissionList) {
                SysPermissionBean permissionBean = new SysPermissionBean();
                BeanUtils.copyProperties(permission, permissionBean);
                permissions.add(permissionBean);
            }
        }

        return new ResultObject<>(200,"success", permissions);
    }

    @ApiOperation(value = "角色权限ID列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/listByRole")
    @PreAuthorize("hasPermission('permission','list')")
    public ResultObject<List<Long>> getPermissionsByRole(@ApiParam(value="Role Id",required=true)
                                                                               @RequestParam long roleId) {

        List<Long> permissionIds = sysPermissionService.selectIdByRoleId(roleId);

        return new ResultObject<>(200,"success", permissionIds);
    }

    @ApiOperation(value = "权限组内权限列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/listByGroupId")
    @PreAuthorize("hasPermission('permission','list')")
    public ResultObject<ArrayList<SysPermissionBean>> getPermissionsByGroupId(@ApiParam(value="权限祖Id",required=true)
                                                              @RequestParam long groupId) {
        ArrayList<SysPermissionBean> permissions = new ArrayList<>();
        List<SysPermission> permissionList = sysPermissionService.selectByGroupId(groupId);

        if (null != permissionList && permissionList.size() > 0) {
            for (SysPermission permission : permissionList) {
                SysPermissionBean permissionBean = new SysPermissionBean();
                permissionBean.setId(permission.getId());
                permissionBean.setCode(permission.getCode());
                permissionBean.setDescription(permission.getDescription());
                permissionBean.setName(permission.getName());
                permissionBean.setStatus(permission.getStatus());
                permissionBean.setIsFinal(permission.getIsFinal());
                permissionBean.setRank(permission.getRank());
                permissionBean.setSysPermissionGroupId(permission.getSysPermissionGroupId());

                permissions.add(permissionBean);
            }
        }

        return new ResultObject<>(200,"success", permissions);

    }

    @ApiOperation(value = "权限组列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/group/list")
    @PreAuthorize("hasPermission('permission','list')")
    public ResultObject<ArrayList<SysPermissionGroupBean>> getPermissionGroups(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication) {
        ArrayList<SysPermissionGroupBean> permissionGroups = new ArrayList<>();
        long userId = sysUserService.getUserIdInToken(authentication);

        List<SysPermission> grantedPermissions = sysPermissionService.selectByUserId(userId);
        boolean hasPermission = false;
        if (null == grantedPermissions || grantedPermissions.size() == 0) {
            return new ResultObject<>(200,"success",permissionGroups);
        }
        for (SysPermission s : grantedPermissions) {
            if (s.getCode().contains(Constant.PERMISSION_GROUP_CODE) && s.getCode().contains(Constant.PERMISSION_QUERY_CODE)) {
                hasPermission = true;
                break;
            }
        }

        if (!hasPermission) {
            return new ResultObject<>(200,"success",permissionGroups);
        }
        List<SysPermissionGroup> permissionGroupList = sysPermissionService.selectGroup();

        if (null != permissionGroupList && 0 < permissionGroupList.size()) {
            for (SysPermissionGroup group : permissionGroupList) {
                SysPermissionGroupBean groupBean = new SysPermissionGroupBean();
                groupBean.setId(group.getId());
                groupBean.setName(group.getName());
                groupBean.setDescription(group.getDescription());
                permissionGroups.add(groupBean);
            }
        }

        return new ResultObject<>(200,"success",permissionGroups);

    }

    @ApiOperation(value = "删除权限祖", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/group/delete")
    @PreAuthorize("hasPermission('permission','delete')")
    public ResultMsg deleteGroupById(@ApiParam(value="data: ID必须填写,其他的空缺",required=true)
                            @RequestBody SysPermissionGroupBean data ) {

        data.normalize();
        ResultMsg result = new ResultMsg();

        if (sysPermissionService.deleteGroupById(data.getId())) {
            result.setCode(200);
        } else {
            result.setCode(300000);
            result.setMsg("need try again");
        }
        return result;
    }

    @ApiOperation(value = "增加权限组", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/group/insert")
    @PreAuthorize("hasPermission('permission','insert')")
    public ResultMsg insertGroup(@RequestHeader(value="Authorization",defaultValue="UTF-8") Authentication authentication,
                            @ApiParam(value="data: 名子必须填写,其他的可以空缺",required=true)
                            @RequestBody SysPermissionGroupBean data ) {

        data.normalize();
        ResultMsg result = new ResultMsg();
        SysPermissionGroup sysPermissionGroup = new SysPermissionGroup();

        String username = null;
        Long userId = 0L;
        String name = data.getName();
        String description = data.getDescription();

        if (name.isEmpty()) {
            result.setCode(300003);
            result.setMsg("名不能为空");
            return result;
        }

        if (sysPermissionService.isExistGroupName(name)) {
            result.setCode(300000);
            result.setMsg("权限组名已经存在");
            return result;
        }

        if (null != authentication && null != authentication.getPrincipal()) {
            username = authentication.getPrincipal().toString();
        }
        if (null != username && !username.isEmpty()) {
            SysUser sysUser = sysUserService.selectByLoginName(username);
            if (null != sysUser) {
                userId = sysUser.getId();
            }
        }

        sysPermissionGroup.setName(name);
        sysPermissionGroup.setDescription(description);
        sysPermissionGroup.setIsFinal(data.getIsFinal());
        sysPermissionGroup.setStatus(1);
        sysPermissionGroup.setRank(1L);
        sysPermissionGroup.setCreateTime(new Date());
        sysPermissionGroup.setUpdateTime(new Date());
        if (userId > 0) {
            sysPermissionGroup.setCreateBy(userId);
            sysPermissionGroup.setUpdateBy(userId);
        }

        long id = sysPermissionService.insertPermissionGroup(sysPermissionGroup);
        result.setCode(200);
        result.setMsg("id:"+id);
        System.out.println("permission insert success");
        return result;
    }

    @ApiOperation(value = "更新权限组信息", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/group/update")
    @PreAuthorize("hasPermission('permission','update')")
    public ResultMsg updateGroupById(@RequestHeader(value="Authorization",defaultValue="UTF-8") Authentication authentication,
                            @ApiParam(value="data: ID必须填写,其他的可以空缺",required=true)
                            @RequestBody SysPermissionGroupBean data ) {

        data.normalize();
        ResultMsg result = new ResultMsg();
        SysPermissionGroup sysPermissionGroup;

        String username = null;
        long userId = 0L;
        long id = data.getId();

        sysPermissionGroup = sysPermissionService.selectGroupById(id);
        if ( null == sysPermissionGroup ) {
            result.setCode(300003);
            result.setMsg("权限组不存在");
            return result;
        }

        if (null != authentication && null != authentication.getPrincipal()) {
            username = authentication.getPrincipal().toString();
        }
        if (null != username && !username.isEmpty()) {
            SysUser sysUser = sysUserService.selectByLoginName(username);
            if (null != sysUser) {
                userId = sysUser.getId();
            }
        }

        sysPermissionGroup.setName(data.getName());
        sysPermissionGroup.setDescription(data.getDescription());
        sysPermissionGroup.setIsFinal(data.getIsFinal());
        sysPermissionGroup.setUpdateTime(new Date());

        if (userId > 0) {
            sysPermissionGroup.setUpdateBy(userId);
        }

        sysPermissionService.updateGroupById(sysPermissionGroup);
        result.setCode(200);
        result.setMsg("success");
        System.out.println("update success");
        return result;
    }

    @ApiOperation(value = "查询权限列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("list")
    @PreAuthorize("hasPermission('permission','list')")
    public ResultObject<PageInfo<SysPermissionBean>> list(
            @ApiParam(value="data:pageIndex, pageSize",required=true)
            @RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
            @RequestBody PageRequestBean<SysPermissionBean> postData) {

        PageInfo<SysPermissionBean> pageNone = new PageInfo<>(0,0,new ArrayList<>());
        long userId = sysUserService.getUserIdInToken(authentication);

        List<SysPermission> grantedPermissions = sysPermissionService.selectByUserId(userId);
        boolean hasPermission = false;
        if (null == grantedPermissions || grantedPermissions.size() == 0) {
            return new ResultObject<>(200,"success", pageNone);
        }
        for (SysPermission s : grantedPermissions) {
            if (s.getCode().contains(Constant.PERMISSION_GROUP_CODE) && s.getCode().contains(Constant.PERMISSION_QUERY_CODE)) {
                hasPermission = true;
                break;
            }
        }

        if (!hasPermission) {
            return new ResultObject<>(200,"success",pageNone);
        }

        Long groupId;
        if (postData.getData().getSysPermissionGroupId() == null) {
            groupId = null;
        } else {
            groupId = new Long(postData.getData().getSysPermissionGroupId());
        }
        PageInfo<SysPermissionBean> pageInfo = sysPermissionService.selectPage(postData.getPageIndex(), postData.getPageSize(), "id", "ASC",
                postData.getData().getName(), postData.getData().getDescription(),
                postData.getData().getCode(), groupId);

        return  new ResultObject<>(200,"success",pageInfo);
    }

}
