package com.software.seller.filter;

//import com.software.seller.service.impl.UserDetailsServiceImpl;
import com.software.seller.security.UsernameIsExitedException;
import com.software.seller.util.JSONResult;
import com.software.seller.security.GrantedAuthorityImpl;
import com.software.seller.service.TokenAuthenticationService;
//import org.springframework.beans.factory.annotation.Autowired;
import com.software.seller.util.StringUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;

    /**
     *
     * @param url 拦截的登陆URL地址
     * @param authenticationManager my Authentication Manager
     */
    public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(url,"POST"));
        this.authenticationManager = authenticationManager;
    }
    /**
     *
     * @param request Http Request
     * @param response Http Response
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        //得到用户登陆信息,并封装到 Authentication 中,供自定义用户组件使用.
        String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        String username = null, password = null,code = null;
        if(StringUtils.hasText(body)) {
            JSONObject jsonObj = JSON.parseObject(body);
            username = jsonObj.getString("username");
            password = jsonObj.getString("password");
            code = jsonObj.getString("code");
        }

        if (null == code || code.isEmpty()) {
            throw new UsernameIsExitedException("verification code is wrong");
        }

        //System.out.println("attemptAuthentication: " + username + " password: " + password);
        if (null == username) {
            username = "";
        }

        if (null == password) {
            password = "";
        }

        username = username.trim();

        String storeCode = StringUtil.getVerificationCode(username);
        if (null == storeCode || !storeCode.equals(code)) {
            throw new UsernameIsExitedException("verification code is wrong");
        }

        ArrayList<GrantedAuthorityImpl> authorities = new ArrayList<>();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, authorities);

        //System.out.println("attemptAuthentication: got token: " + authenticationToken );
        return this.authenticationManager.authenticate(authenticationToken);

    }

    /**
     * 登陆成功后,此方法会被调用,因此我们可以在次方法中生成token,并返回给客户端
     *
     * @param request Http request
     * @param response Http response
     * @param chain  Filter chain
     * @param authResult result of authentication
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException {
        //System.out.println("successfulAuthentication");
        TokenAuthenticationService.addAuthenticatiotoHttpHeader(response,authResult);

        //String username = authResult.getPrincipal().toString();

        //StringUtil.setToken(username, authResult.toString());
        response.getWriter().println(JSONResult.fillResultString(200,"success", "in Header, Authorization Bearer Token"));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        //response.getOutputStream().println(JSONResult.fillResultString(500, failed.getMessage(), "null"));
        response.getWriter().println(JSONResult.fillResultString(500, failed.getMessage(), "null"));
    }
}
