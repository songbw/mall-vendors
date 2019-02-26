package com.software.seller.service.impl;

//import com.software.seller.bean.SysUserBean;
import com.software.seller.bean.UserDetailsBean;

import com.software.seller.model.SysUser;
import com.software.seller.mapper.SysUserMapper;
import com.software.seller.mapper.SysUserPermissionMapper;
import com.software.seller.mapper.SysPermissionMapper;
import com.software.seller.security.UsernameIsExitedException;
//import org.apache.ibatis.annotations.Param;
//import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//import com.software.seller.util.PasswordEncodeUtil;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserPermissionMapper sysUserPermissionMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    //private String tmpPass = PasswordEncodeUtil.passwordEncoder("111");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("===loadUserByUsername: " + username);
        if (null == username) { throw new UsernameIsExitedException("user name is wrong");}
        UserDetailsBean userDetailsBean;
        if ( ! sysUserMapper.isExistLoginNameByName(username)) {
            //System.out.println("==========loadUserByUsername: can not find username");
            throw new UsernameIsExitedException("user name is wrong");
        }

        SysUser sysUser = sysUserMapper.selectUserByLoginName(username);
        //System.out.println("find user : " + sysUser.getLoginName() + " password: "+sysUser.getPassword());
        userDetailsBean = new UserDetailsBean(username,sysUser.getPassword(),sysUser.getStatus());
        System.out.println("===loadUserByUsername: find username: " + username +" id: "+sysUser.getId() +" password: "+sysUser.getPassword());

        //自定义用户存储数据来源，可以是从关系型数据库，非关系性数据库，或者其他地方获取用户数据。
        //UserDetailsBean userDetailsBean = sysUserMapper.selectInfoByLoginName(username);
        //UserDetailsBean userDetailsBean = new UserDetailsBean("111",tmpPass,1);
        //还可以在此设置账号的锁定,过期,凭据失效 等参数
        //...

        // 设置权限
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();

        List<String> permissionList = sysPermissionMapper.selectCodeByUserId(sysUser.getId());
        System.out.println("got user permission : "+ permissionList);
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        userDetailsBean.setAuthorities(authorities);
        System.out.println("==========loadUserByUsername: done : " + userDetailsBean );
        return userDetailsBean;
    }
}

