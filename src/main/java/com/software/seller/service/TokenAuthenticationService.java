package com.software.seller.service;

import com.software.seller.util.StringUtil;
//import com.software.seller.security.UsernameIsExitedException;
//import com.software.seller.security.GrantedAuthorityImpl;
import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TokenAuthenticationService {

    /**
     * 过期时间 2小时
     */
    private static final long EXPIRATIONTIME = 1000 * 60 * 60 * 2;
    /**
     * JWT 密码
     */
    private static final String SECRET = "yearcon";
    /**
     * TOKEN前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";
    /**
     * 存放Token的Header Key
     */
    private static final String HEADER_STRING = "Authorization";

    /**
     * 自定义的 playload
     */
    private static final String AUTHORITIES = "authorities";

    /**
     * 将jwt token 写入header头部
     *
     * @param response Http response
     * @param authentication authentication from Filter
     */
    public static void addAuthenticatiotoHttpHeader(HttpServletResponse response, Authentication authentication) {
        //Generate jwt

       // Claims claims = (Claims) Jwts.claims().put("aName", "aValue");

        String token = Jwts.builder()
                //生成token的时候可以把自定义数据加进去,比如用户权限
                //.claim(AUTHORITIES, "ROLE_ADMIN,AUTH_WRITE")
                .setSubject(authentication.getName())
//                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        //System.out.println("user: "+authentication.getName() +"  add header token: " + token);
        StringUtil.setToken(authentication.getName(), token);
        //把token设置到响应头中去
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }

    /**
     * 从请求头中解析出 Authentication
     * @param request Http request
     * @return authentication info
     */
    public static Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // 从Header中拿到token
        //System.out.println("try get token from request : "+request.getHeader("Authorization"));
        String token = request.getHeader(HEADER_STRING);
        if(token==null){
            return null;
        }

        //may throw exception
        Claims claims = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();

        //String auth = (String)claims.get(AUTHORITIES);

        // 得到 权限（角色）
        List<GrantedAuthority> authorities =  AuthorityUtils.
                commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES));


        String username = claims.getSubject();
        if (null == username || username.isEmpty()) {
            return null;
        }
        String receivedToken = token.substring(7);
        String storedToken = StringUtil.getToken(username);
        //System.out.println("received token: " + receivedToken);
        //System.out.println("stored token : " + storedToken);
        if (null == storedToken || storedToken.isEmpty() || !storedToken.equals(receivedToken)) {
            System.out.println("error token expired");
            return null;
        }

        //得到过期时间
        Date expiration = claims.getExpiration();
        Date now = new Date();

        if (now.getTime() > (expiration.getTime() - (EXPIRATIONTIME/2))) {
            System.out.println("-=-=-=- should refresh Token: now= " + now.getTime() + " expir= " + expiration.getTime()/2);
            addAuthenticatiotoHttpHeader(response, new UsernamePasswordAuthenticationToken(username, null, authorities));
        }

        return new UsernamePasswordAuthenticationToken(username, null, authorities);

    }


}


