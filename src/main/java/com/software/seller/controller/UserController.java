package com.software.seller.controller;

import com.software.seller.util.Constant;
import com.software.seller.bean.LoginBean;
import com.software.seller.bean.UserRegistryBean;
import com.software.seller.bean.SysUserBean;
import com.software.seller.bean.SysUserSearchBean;
import com.software.seller.bean.SysUserPermissionBean;
//import com.software.seller.config.SMSConfig;
//import com.software.seller.model.Login;
import com.software.seller.model.SysUser;
import com.software.seller.service.impl.SysUserServiceImpl;
import com.software.seller.service.impl.SysPermissionServiceImpl;
import com.software.seller.util.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
//import java.util.Map;

@Api(tags="userAPI", description = "用户管理相关", produces = "application/json;charset=UTF-8")
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    private SysUserServiceImpl sysUserService;
    private SysPermissionServiceImpl sysPermissionService;

    @Autowired
    public UserController(SysUserServiceImpl sysUserService, SysPermissionServiceImpl sysPermissionService){

        this.sysUserService = sysUserService;
        this.sysPermissionService = sysPermissionService;
    }

    @ApiOperation(value = "注册", notes="需要先获取验证码")
    @ApiImplicitParams({

    })
    @PostMapping("/registry")
    public ResultMsg registry(@ApiParam(value="data:用户名,密码,验证码 必须填写,其他的可以空缺",required=true)
                              @RequestBody UserRegistryBean data) {

        data.normalize();
        ResultMsg result = new ResultMsg();
        SysUser sysUser = new SysUser();
        String name = data.getLoginName();
        String password = data.getPassword();
        String code = data.getCode();
        String zhName = data.getZhName();
        String enName = data.getEnName();
        Integer sex = data.getSex();
        String birth= data.getBirth();
        String email= data.getEmail();
        String phone= data.getPhone();
        String address= data.getAddress();
        String createBy= Constant.NO_ONE_ID;
        List<Long> permissions = new ArrayList<>();
        Long userRoleOrganization = Constant.NO_ORGANIZATION_ID;

        permissions.add(Constant.NO_PERMISSION_ID);

        if (!StringUtil.isRightName(name)) {
            result.setCode(100000);
            result.setMsg("用户名只能输入4-20个以字母开头、可带数字、“_”、“.”的字串");
            return result;
        }

        if (!StringUtil.isRightPassword(password)) {
            result.setCode(100010);
            result.setMsg("密码只能输入4-20个以字母开头、可带数字、“_”、“.”的字串");
            return result;
        }

        if (sysUserService.isExistLoginName(name)) {
            result.setCode(300000);
            result.setMsg("用户名已经存在");
            return result;
        }

        String storeCode = StringUtil.getVerificationCode(name);
        if (null == storeCode || !storeCode.equals(code)) {
            result.setCode(300001);
            result.setMsg("验证码错误");
            return result;
        }

        sysUser.setLoginName(name);
        sysUser.setAddress(address);
        sysUser.setBirth(birth);
        sysUser.setCreateBy(Long.parseLong(createBy));
        sysUser.setUpdateBy(Long.parseLong(createBy));
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(sysUser.getCreateTime());
        sysUser.setEmail(email);
        sysUser.setEnName(enName);
        sysUser.setPhone(phone);
        sysUser.setSex(sex);
        sysUser.setPassword(PasswordEncodeUtil.passwordEncoder(password));
        sysUser.setZhName(zhName);
        sysUser.setStatus(1);
        sysUser.setIsFinal(0);
        sysUser.setRank(1L);
        long id = sysUserService.insertUser(sysUser,permissions,userRoleOrganization);

        result.setCode(200);
        result.setMsg("id:"+id);
        //System.out.println("注册成功");
        return result;
    }

    @ApiOperation(value = "注册", notes="需要先获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/insert")
    @PreAuthorize("hasPermission('user','insert')")
    public ResultMsg insert(@ApiParam(value="data:用户名,密码,必须填写,其他的可以空缺",required=true)
                                @RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                                @RequestBody SysUserBean data) {

        data.normalize();
        long currentUserId = sysUserService.getUserIdInToken(authentication);
        ResultMsg result = new ResultMsg();
        SysUser sysUser = new SysUser();

        String name = data.getLoginName();
        String password = data.getPassword();
        String zhName = data.getZhName();
        String enName = data.getEnName();
        Integer sex = data.getSex();
        String birth= data.getBirth();
        String email= data.getEmail();
        String phone= data.getPhone();
        String address= data.getAddress();
        List<Long> permissions = data.getPermissionIds();
        Long userRoleOrganization = data.getOrganizationId();

        if (!StringUtil.isRightName(name)) {
            result.setCode(100000);
            result.setMsg("用户名只能输入4-20个以字母数字开头、可带数字、“_”、“.”的字串");
            return result;
        }

        if (!StringUtil.isRightPassword(password)) {
            result.setCode(100010);
            result.setMsg("密码只能输入4-20个字母数字开头、可带数字、“_”、“.”的字串");
            return result;
        }

        if (sysUserService.isExistLoginName(name)) {
            result.setCode(300000);
            result.setMsg("用户名已经存在");
            return result;
        }

        sysUser.setLoginName(name);
        sysUser.setAddress(address);
        sysUser.setBirth(birth);
        sysUser.setCreateBy(currentUserId);
        sysUser.setUpdateBy(currentUserId);
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(new Date());
        sysUser.setEmail(email);
        sysUser.setEnName(enName);
        sysUser.setPhone(phone);
        sysUser.setSex(sex);
        sysUser.setPassword(PasswordEncodeUtil.passwordEncoder(password));
        sysUser.setZhName(zhName);
        sysUser.setStatus(1);
        sysUser.setIsFinal(0);
        sysUser.setRank(1L);
        long id = sysUserService.insertUser(sysUser,permissions,userRoleOrganization);

        result.setCode(200);
        result.setMsg("id:"+id);

        //System.out.println("Insert success");
        return result;
    }

    @ApiOperation(value = "获取用户信息", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @GetMapping("/info")
    //@PreAuthorize("hasPermission('user','list')")
    public ResultObject<SysUserBean> getUserInfo(@ApiParam(value="用户名",required=true)@RequestParam String username) {

        ResultObject<SysUserBean> result = new ResultObject<>(200,"success",null);
        if (null == username || !sysUserService.isExistLoginName(username)) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }
        SysUser sysUser = sysUserService.selectByLoginName(username);
        SysUserBean sysUserBean = new SysUserBean();
        sysUserBean.setId(sysUser.getId());
        sysUserBean.setLoginName(sysUser.getLoginName());
        sysUserBean.setZhName(sysUser.getZhName());
        sysUserBean.setEnName(sysUser.getEnName());
        sysUserBean.setSex(sysUser.getSex());
        sysUserBean.setBirth(sysUser.getBirth());
        sysUserBean.setEmail(sysUser.getEmail());
        sysUserBean.setPhone(sysUser.getPhone());
        sysUserBean.setAddress(sysUser.getAddress());
        sysUserBean.setCreateTime(sysUser.getCreateTime().toString());
        sysUserBean.setUpdateTime(sysUser.getUpdateTime().toString());

        //sysUserBean.setUserRoleOrganizations(sysOrganizationService.selectOrganizationByUserId(sysUser.getId()));
        sysUserBean.setPermissionCodes(sysPermissionService.selectCodeByUserId(sysUser.getId()));
        //sysUserBean.setUserRoles(sysRoleService.selectRoleByUserId(sysUser.getId()).toString());
        result.setCode(200);
        result.setMsg("success");
        result.setData(sysUserBean);

        return result;
    }

    @ApiOperation(value = "删除用户", notes="需要管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/deleteByName")
    @PreAuthorize("hasPermission('user','delete')")
    public ResultMsg delUserByName(@ApiParam(value="data:用户名,必须填写,其他的可以空缺",required=true)
                              @RequestBody LoginBean data) {

        data.normalize();
        ResultMsg result = new ResultMsg();
        SysUser sysUser;
        String name = data.getUsername();

        if (null == name || !sysUserService.isExistLoginName(name)) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }
        sysUser = sysUserService.selectByLoginName(name);
        if (null == sysUser) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }

        sysUserService.deleteById(sysUser.getId());
        result.setCode(200);
        //System.out.println("删除成功");
        return result;
    }

    @ApiOperation(value = "更新用户信息", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/update")
    @PreAuthorize("hasPermission('user','update')")
    public ResultMsg update(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                            @ApiParam(value="data:用户名 必须填写,其他的可以空缺",required=true)
                            @RequestBody SysUserBean data) {

        data.normalize();
        long userId = sysUserService.getUserIdInToken(authentication);
        ResultMsg result = new ResultMsg();
        SysUser sysUser ;
        Long id = data.getId();
        String name = data.getLoginName();
        String password = data.getPassword();
        String zhName = data.getZhName();
        String enName = data.getEnName();
        Integer sex = data.getSex();
        String birth= data.getBirth();
        String email= data.getEmail();
        String phone= data.getPhone();
        String address= data.getAddress();
        List<Long> permissions = data.getPermissionIds();
        Long userRoleOrganization = data.getOrganizationId();

        if (null == name || !sysUserService.isExistLoginName(name)) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }

        sysUser = sysUserService.selectByLoginName(name);
        if (null == sysUser) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }

        sysUser.setId(id);
        sysUser.setLoginName(name);
        sysUser.setAddress(address);
        sysUser.setBirth(birth);
        sysUser.setEmail(email);
        sysUser.setEnName(enName);
        sysUser.setPhone(phone);
        sysUser.setSex(sex);
        if (null != password && !password.isEmpty()) {
            if (!StringUtil.isRightPassword(password)) {
                result.setCode(100010);
                result.setMsg("密码只能输入4-20个字母数字开头、可带数字、“_”、“.”的字串");
                return result;
            }
            sysUser.setPassword(PasswordEncodeUtil.passwordEncoder(password));
        }
        sysUser.setZhName(zhName);
        sysUser.setStatus(1);
        sysUser.setUpdateBy(userId);
        sysUser.setUpdateTime(new Date());
        sysUser.setIsFinal(0);
        sysUser.setRank(1L);

        sysUserService.updateUser(sysUser,permissions,userRoleOrganization);

        result.setCode(200);
        //System.out.println("更新成功");
        return result;
    }

    @ApiOperation(value = "更新用户权限", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/permission/update")
    @PreAuthorize("hasPermission('user','update')")
    public ResultMsg updatePermission(@ApiParam(value="data:用户ID, 权限ID",required=true)
                            @RequestBody SysUserPermissionBean data) {

        data.normalize();
        ResultMsg result = new ResultMsg();

        Long userId = data.getSysUserId();
        List<Long> permissionIds = data.getRolePermissionIds();

        sysUserService.updateUserPermission(userId,permissionIds);

        result.setCode(200);
        //System.out.println("更新成功");
        return result;
    }

    @ApiOperation(value = "查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("list")
    @PreAuthorize("hasPermission('user','list')")
    public ResultObject<PageInfo<SysUserBean>> list(@ApiParam(value="data:搜索条件",required=true)
                                                        @RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                                                        @RequestBody SysUserSearchBean postData) {

        Integer index = 1;
        Integer size = 10;
        long currentUserId = sysUserService.getUserIdInToken(authentication);
        Long rank = sysUserService.selectOrganizationRank(currentUserId);
        ResultObject<PageInfo<SysUserBean>> result = new ResultObject<>(200,"success",null);
        String loginName = null, zhName=null, email=null, phone=null, address=null;
        java.util.Date createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd;

        createTimeStart = new Date(1L);
        createTimeEnd = new Date();
        updateTimeStart = new Date(1L);
        updateTimeEnd = new Date();

        if (null != postData){
            loginName = postData.getLoginName();
            zhName = postData.getZhName();
            email= postData.getEmail();
            phone = postData.getPhone();
            address = postData.getAddress();
            //System.out.println("=== loginName: " + loginName + " Id: " + currentUserId + " rank: " + rank);
            try {
                if (null != postData.getCreateTimeStart() && !postData.getCreateTimeStart().isEmpty()) {
                    createTimeStart = StringUtil.String2Date(postData.getCreateTimeStart());
                }
                if (null != postData.getCreateTimeEnd() && !postData.getCreateTimeEnd().isEmpty()) {
                    createTimeEnd = StringUtil.String2Date(postData.getCreateTimeEnd());
                }
                if (null != postData.getUpdateTimeStart() && !postData.getUpdateTimeStart().isEmpty()) {
                    updateTimeStart = StringUtil.String2Date(postData.getUpdateTimeStart());
                }
                if (null != postData.getUpdateTimeEnd() && !postData.getUpdateTimeEnd().isEmpty()) {
                    updateTimeEnd = StringUtil.String2Date(postData.getUpdateTimeEnd());
                }
            } catch (ParseException ex) {
                System.out.println("parse error");
            }

            try {
                index = postData.getPageIndex();
                size = postData.getPageSize();
            } catch (NullPointerException ex) {
                index = 1;
                size = 10;
            }

        } else {
            System.out.println("!! can not find post data");
        }

        /*System.out.println(" loginName = [" + loginName + "], zhName = [" + zhName + "], email = [" + email + "], phone = [" + phone + "]," +
                " address = [" + address + "]" + " createTimeStart = " + createTimeStart + "]" +
                " createTimeEnd = " + createTimeEnd + "]" +
                " updateTimeStart = " + updateTimeStart + "]" +
                " updateTimeEnd = " + updateTimeEnd + "]" );*/

        PageInfo<SysUserBean> pageInfo = sysUserService.selectPage(index, size,
                "id", "ASC", loginName, zhName, email, phone, address, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd, rank);

        result.setData(pageInfo);

        return result;
    }
}

