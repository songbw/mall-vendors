package com.software.seller.service.impl;

import com.software.seller.security.UsernameIsExitedException;
//import com.software.seller.service.impl.UserDetailsServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
import com.software.seller.util.PasswordEncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.util.DigestUtils;

import java.util.ArrayList;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // System.out.println("support it");
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    /**
     * 验证登录信息,若登陆成功,设置 Authentication
     *
     * @param authentication authentication from filter
     * @return 一个完全经过身份验证的对象，包括凭证。
     * 如果AuthenticationProvider无法支持已通过的身份验证对象的身份验证，则可能返回null。
     * 在这种情况下，将会尝试支持下一个身份验证类的验证提供者。
     * @throws AuthenticationException exception
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        //System.out.println("authenticate: " + username + "  password: " + password);
        //通过用户名从数据库中查询该用户
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String dbPassword = userDetails.getPassword();

        if (!PasswordEncodeUtil.isMatch(password,dbPassword)) {
            System.out.println("authenticate: password is wrong!! " );
            throw new UsernameIsExitedException("password is wrong");
        }

        //System.out.println("authenticate: password is right. " );

        // 还可以从数据库中查出该用户所拥有的权限,设置到 authorities 中去,这里模拟数据库查询.
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        // authorities.add(new GrantedAuthorityImpl("ADMIN"));

        Authentication auth = new UsernamePasswordAuthenticationToken(username, password, authorities);
        //SecurityContextHolder.getContext().setAuthentication(auth);
        System.out.println("authenticate done");
        return auth;

    }

}
