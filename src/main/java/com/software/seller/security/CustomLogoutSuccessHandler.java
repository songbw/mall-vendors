package com.software.seller.security;

import com.software.seller.util.JSONResult;
import com.software.seller.util.RedisUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

//import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
    @Override
    public void onLogoutSuccess(HttpServletRequest var1, HttpServletResponse var2, Authentication var3)
            throws IOException {
        System.out.println("success logout");


        if (null == var3|| null == var3.getPrincipal()) {
            var2.getWriter().println(JSONResult.fillResultString(10004,"invalid Token", ""));
            return;
        }
        String username = var3.getPrincipal().toString();
        if (null == username || username.isEmpty()) {
            var2.getWriter().println(JSONResult.fillResultString(10002,"invalid username", ""));
            return;
        }
        RedisUtil.removeValue(username);

        new HttpStatusReturningLogoutSuccessHandler();
        var2.getWriter().println(JSONResult.fillResultString(200,"success", ""));
    }

}
