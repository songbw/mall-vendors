package com.software.seller.filter;

import com.software.seller.service.TokenAuthenticationService;
import com.software.seller.util.JSONResult;
//import com.software.seller.util.StringUtil;
import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.JwtParser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager)
    {
        super(authenticationManager);
    }


    /**
     * 在此方法中检验客户端请求头中的token,
     * 如果存在并合法,就把token中的信息封装到 Authentication 类型的对象中,
     * 最后使用  SecurityContextHolder.getContext().setAuthentication(authentication); 改变或删除当前已经验证的 pricipal
     *
     * @param request http request
     * @param response http response
     * @param chain filter chain
     * @throws IOException exception
     * @throws ServletException exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //System.out.println("doFilterInternal");
        if (request.getRequestURI().contains("/api/swagger-ui.html")) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication;
        try {
            authentication = TokenAuthenticationService.getAuthentication(request, response);
        } catch (JwtException ex) {
            //System.out.println("doFilterInternal: token Exp");
            response.getWriter().println(JSONResult.fillResultString(5000, "failed", ex.getMessage()));
            return;
        }

        if (null == authentication) {
            //System.out.println("doFilterInternal: no token");
            chain.doFilter(request, response);
            return;
        }
/*
* Token :org.springframework.security.authentication.UsernamePasswordAuthenticationToken@fa79fc8b: Principal: admin; Credentials: [PROTECTED]; Authenticated: true; Details: null; Not granted any authorities
* */
/*
        System.out.println("Token :" + authentication);
        String userName;
        if (null == authentication.getPrincipal()) {
            System.out.println("doFilterInternal: token no username");
            response.getWriter().println(JSONResult.fillResultString(5000, "failed", "not username"));
            return;
        }
        userName = authentication.getPrincipal().toString();
        String storeToken = StringUtil.getToken(userName);
        System.out.println("stored token:   " + storeToken);
        System.out.println("received token: " + authentication.toString());
        if (null == storeToken || !storeToken.equals(authentication.toString())) {
            System.out.println("doFilterInternal: invalid authentication");
            response.getWriter().println(JSONResult.fillResultString(5000, "failed", "invalid authentication"));
            return;
        }
        System.out.println("AutherFilter: got token from for :"+userName);
     */
/*
        if (null == SecurityContextHolder.getContext()){
            System.out.println("doFilterInternal: not found context");
            response.getWriter().println(JSONResult.fillResultString(500, "failed", "anthentication error"));
            return;
        }

        Authentication storeAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == storeAuthentication || null == storeAuthentication.getPrincipal() ||
                !storeAuthentication.isAuthenticated())  {
            System.out.println("doFilterInternal: invalid authentication");
            response.getWriter().println(JSONResult.fillResultString(500, "failed", "invalid authentication"));
            return;
        }

        System.out.println("doFilterInternal: get username: "+storeAuthentication.getPrincipal());
*/
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //System.out.println("doFilterInternal: gotContext and do filter");
        chain.doFilter(request, response);

    }
}

