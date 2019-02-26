package com.software.seller.security;

import com.software.seller.model.SysPermission;
import com.software.seller.model.SysUser;
import com.software.seller.service.impl.SysPermissionServiceImpl;
import com.software.seller.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
//import java.util.Collection;
import java.util.List;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator{

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysPermissionServiceImpl sysPermissionService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object targetPermission) {
        // 获得loadUserByUsername()方法的结果
        String userName = authentication.getPrincipal().toString();
        //System.out.println("hasPermission : " + userName);
        SysUser user = sysUserService.selectByLoginName(userName);
        List<SysPermission> permissions = sysPermissionService.selectByUserId(user.getId());
        StringBuilder sb = new StringBuilder();

        sb.append(targetUrl);
        sb.append(":");
        sb.append(targetPermission);
        String requirePermission = sb.toString();

        if (null != permissions && 0 < permissions.size()) {
            for (SysPermission s : permissions) {
                //System.out.println("has permission : " + s.getCode() );
                if (s.getCode().contains(requirePermission)) {
                    //System.out.println("hasPermission: got permission");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }

}
