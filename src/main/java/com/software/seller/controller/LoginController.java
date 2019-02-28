package com.software.seller.controller;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
import com.software.seller.bean.LoginBean;
import com.software.seller.util.*;
import com.software.seller.model.SysUser;
import com.software.seller.service.impl.SysUserServiceImpl;
import com.software.seller.util.PasswordEncodeUtil;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//import java.nio.charset.Charset;
//import java.text.ParseException;
import java.util.Date;
//import java.util.ArrayList;
//import java.util.List;

@Api(tags="LoginAPI", description = "用户登录相关", produces = "application/json;charset=UTF-8")
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {

    private SysUserServiceImpl sysUserService;


    @Autowired
    public LoginController(SysUserServiceImpl sysUserService) {
        this.sysUserService = sysUserService;
    }

    // private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @ApiOperation(value = "登录", notes="返回的Header中Token需要保存，用于其他操作的Authorization")
    @PostMapping("/login")
    public ResultMsg login(@ApiParam(value="用户名,密码,验证码",required=true)@RequestBody LoginBean loginBean)
    {//Only used by Swagger to create document
        ResultMsg result = new ResultMsg();
        loginBean.normalize();

        result.setCode(200);
        return result;
    }

    @ApiOperation(value = "登出", notes="密码不需要")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/userLogout")
    public ResultMsg userLogout(@ApiParam(value="body: 用户名",required=true)@RequestBody LoginBean username)
    {
        //System.out.println("logout");
        ResultMsg result = new ResultMsg();
        if (null == username || null == username.getUsername()) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }

        String name = username.getUsername();
        if (null == name || name.isEmpty()) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }

        StringUtil.deleteToken(name);
        //System.out.println("removed the key: "+name);
        result.setCode(200);
        return result;
    }

    @ApiOperation(value = "获取验证码", notes="验证码在返回JSON中的msg部分")
    @GetMapping("/getCode")
    public ResultMsg getVerificationCode(@ApiParam(value="用户名",required=true)@RequestParam String username) {
        //System.out.println("getCode for "+username);
        ResultMsg result = new ResultMsg();
        if(null == username || username.isEmpty()){
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }
        String code = SMSUtil.getFourRandom();
        // String[] params = {code,SMSConfig.TENT_ActiveTime};
        // String string = smsUtil.sendMesModel(params, username,SMSConfig.TENT_TemplateID1);

        result.setCode(200);
        result.setMsg(code);
        StringUtil.setVerificationCode(username,code);
        return result;
    }


    @ApiOperation(value = "修改密码", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/password/update")
    @PreAuthorize("hasPermission('user','update')")
    public ResultMsg updatePassword(@ApiParam(value="data:用户名,旧密码,新密码",required=true)@RequestBody LoginBean loginBean) {
        ResultMsg result = new ResultMsg();

        loginBean.normalize();
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();
        String oldPassword = loginBean.getOldPassword();
        if (username.isEmpty()) {
            result.setCode(100000);
            result.setMsg("用户名不可为空");
            return result;
        }

        if (oldPassword.isEmpty()) {
            result.setCode(100005);
            result.setMsg("旧密码不可为空");
            return result;
        }

        if (!StringUtil.isRightPassword(password)) {
            result.setCode(100010);
            result.setMsg("密码只能输入6-20个以字母开头、可带数字、“_”、“.”的字串");
            return result;
        }

        SysUser sysUser = sysUserService.selectByLoginName(username);
        if (null == sysUser) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }
        String encodePassword = PasswordEncodeUtil.passwordEncoder(oldPassword);
        if (!encodePassword.equals(sysUser.getPassword())) {
            result.setCode(100006);
            result.setMsg("当前密码不正确");
            return result;
        }

        sysUser.setUpdateTime(new Date());
        sysUser.setPassword(PasswordEncodeUtil.passwordEncoder(password));

        sysUserService.updateUser(sysUser,null,null);

        result.setCode(200);
        return  result;
    }

    @ApiOperation(value = "忘记密码重设", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/password/forget")
    @PreAuthorize("hasPermission('user','update')")
    public ResultMsg forgetPassword(@ApiParam(value="data:用户名,新密码,验证码",required=true)@RequestBody LoginBean loginBean) {
        ResultMsg result = new ResultMsg();

        loginBean.normalize();
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();
        String code = loginBean.getCode();
        if (username.isEmpty()) {
            result.setCode(100000);
            result.setMsg("用户名不可为空");
            return result;
        }

        if (code.isEmpty()) {
            result.setCode(100005);
            result.setMsg("验证码不可为空");
            return result;
        }

        if (!StringUtil.isRightPassword(password)) {
            result.setCode(100010);
            result.setMsg("密码只能输入6-20个字母、可带数字组成的字串，需要同时含有数字和字母");
            return result;
        }

        SysUser sysUser = sysUserService.selectByLoginName(username);
        if (null == sysUser) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }

        String value = StringUtil.getVerificationCode(username);
        if(null == value || !value.equals(code)){
            //System.out.println("storedCode= "+ value);
            result.setCode(100004);
            result.setMsg("验证码不正确");
            return result;
        }

        sysUser.setUpdateTime(new Date());
        sysUser.setPassword(PasswordEncodeUtil.passwordEncoder(password));

        sysUserService.updateUser(sysUser,null,null);
        return  result;
    }

}

