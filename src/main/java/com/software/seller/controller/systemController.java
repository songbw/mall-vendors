package com.software.seller.controller;

import com.software.seller.bean.LoginBean;
import com.software.seller.bean.PageRequestBean;
import com.software.seller.service.impl.SysUserServiceImpl;
import com.software.seller.service.impl.SystemServiceImpl;
import com.software.seller.bean.SysLogBean;
import com.software.seller.model.SysLog;
import com.software.seller.util.PageInfo;
import com.software.seller.util.ResultMsg;
import com.software.seller.util.ResultObject;
import com.software.seller.util.StringUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Api(tags="LogAPI", description = "日志相关", produces = "application/json;charset=UTF-8")
@RestController
@RequestMapping(value = "/log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class systemController {

    private SystemServiceImpl systemService;
    private SysUserServiceImpl sysUserService;

    @Autowired
    public systemController(SystemServiceImpl systemService, SysUserServiceImpl sysUserService) {
        this.systemService = systemService;
        this.sysUserService = sysUserService;
    }

    @ApiOperation(value = "组织列表", notes="Header中必须包含Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/search")
    @PreAuthorize("hasPermission('log','list')")
    public ResultObject<PageInfo<SysLogBean>> searchLog(@ApiParam(value="data:pageIndex, pageSize",required=true)
                                                      @RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication,
                                                      @RequestBody PageRequestBean<SysLogBean> postData) {

        Integer pageIndex = 1;
        Integer pageSize = 10;
        String user = null, method=null, url=null, param=null;
        java.util.Date createTimeStart, createTimeEnd;
        createTimeStart = new Date(1L);
        createTimeEnd = new Date();
        // long currentUserId = sysUserService.getUserIdInToken(authentication);

        if (null != postData){
            method = postData.getData().getMethod();
            url = postData.getData().getUrl();
            param = postData.getData().getParam();
            user = postData.getData().getUser();

            try {
                pageIndex = postData.getPageIndex();
                pageSize = postData.getPageSize();
            } catch (NullPointerException ex) {
                pageIndex = 1;
                pageSize = 10;
            }
        } else {
            System.out.println("!! can not find post data");
            return new ResultObject<>(30000,"missing data",null);
        }
        //System.out.println("search Log: time_start: " + createTimeStart + "   time_end:  " + createTimeEnd);
        try {
            if (null != postData.getData().getCreateTimeStart() && !postData.getData().getCreateTimeStart().isEmpty()) {
                createTimeStart = StringUtil.String2Date(postData.getData().getCreateTimeStart());
            }
            if (null != postData.getData().getCreateTimeEnd() && !postData.getData().getCreateTimeEnd().isEmpty()) {
                createTimeEnd = StringUtil.String2Date(postData.getData().getCreateTimeEnd());
            }
        } catch (ParseException ex) {
            System.out.println("parse error");
            return new ResultObject<>(30000,"missing data",null);
        }

        //System.out.println("search Log: time_start: " + createTimeStart + "   time_end:  " + createTimeEnd);
        PageInfo<SysLogBean> pageInfo = systemService.selectLog(pageIndex, pageSize,
                "id", "DESC", method, url, param, createTimeStart, createTimeEnd, user);

        return new ResultObject<>(200,"success",pageInfo);
    }

    @ApiOperation(value = "删除用户", notes="需要管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/deleteById")
    @PreAuthorize("hasPermission('data','delete')")
    public ResultMsg deleteById(@ApiParam(value="data:id必须填写,其他的空缺",required=true)
                                   @RequestBody SysLogBean data) {

        ResultMsg result = new ResultMsg();
        if (null != data && null != data.getId()) {
            systemService.deleteLogById(data.getId());

            result.setCode(200);
            result.setMsg("success");
        } else {
            result.setCode(30000);
            result.setMsg("missing data");
        }
        return result;
    }

    @ApiOperation(value = "删除用户组", notes="需要管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/deleteByIdList")
    @PreAuthorize("hasPermission('data','delete')")
    public ResultMsg deleteByIdList(@ApiParam(value="data:idList必须填写,其他空缺",required=true)
                                @RequestBody SysLogBean data) {

        ResultMsg result = new ResultMsg();
        if (null != data.getIdList() && 0 < data.getIdList().size()) {
            systemService.deleteLogByIdList(data.getIdList());

            result.setCode(200);
            result.setMsg("success");
        } else {
            result.setCode(30000);
            result.setMsg("missing data");
        }
        return result;
    }

    @ApiOperation(value = "数据清理", notes="需要管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Token",value="登录成功时从服务获得的Token",required=true, paramType="Header",dataType="String")
    })
    @PostMapping("/shrink")
    @PreAuthorize("hasPermission('db','delete')")
    public ResultMsg shrinkAll(@RequestHeader(value="Authorization",defaultValue="UTF-8") String authentication) {

        ResultMsg result = new ResultMsg();
        long currentUserId = sysUserService.getUserIdInToken(authentication);
        if (0 < currentUserId) {
            systemService.shrinkTables();

            result.setCode(200);
            result.setMsg("success");
        } else {
            result.setCode(30000);
            result.setMsg("missing data");
        }
        return result;
    }
}
