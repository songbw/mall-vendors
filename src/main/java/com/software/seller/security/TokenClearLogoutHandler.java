package com.software.seller.security;

//import com.software.seller.service.TokenAuthenticationService;
//import com.software.seller.service.impl.UserDetailsServiceImpl;

//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.software.seller.util.JSONResult;
//import com.software.seller.util.RedisUtil;
//import org.apache.http.HttpException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;

//import java.io.IOException;


public class TokenClearLogoutHandler implements LogoutHandler {

    //@Autowired
    //private UserDetailsServiceImpl userDetailsService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication)
             {
        System.out.println("LogoutHander: logout");
        /*
        SecurityContextHolder.clearContext();

        if (null == authentication || null == authentication.getPrincipal()) {
            return;
        }
        String username = authentication.getPrincipal().toString();
        if (null == username || username.isEmpty()) {
            return;
        }
        RedisUtil.removeValue(username);
        */
        //UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, authorities);

        //authenticate(authenticationToken);

    }

}

