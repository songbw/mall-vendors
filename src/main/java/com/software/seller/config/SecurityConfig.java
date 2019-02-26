package com.software.seller.config;

import com.software.seller.service.impl.UserDetailsServiceImpl;
import com.software.seller.service.impl.CustomAuthenticationProvider;
import com.software.seller.security.TokenClearLogoutHandler;
//import com.software.seller.security.CustomLogoutSuccessHandler;
//import com.software.seller.util.JSONResult;
import com.software.seller.filter.*;
import com.software.seller.security.CustomPermissionEvaluator;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
//import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
//import org.springframework.security.access.expression.SecurityExpressionOperations;
//import org.springframework.security.authentication.AuthenticationTrustResolver;
//import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.util.Assert;

//import org.springframework.security.access.PermissionEvaluator;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.header.Header;
//import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true,prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
/*
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService detailsService() {
        return new UserDetailsServiceImpl();
    }

*/
   // @Autowired
  //  private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(detailsService()) // 用户认证
        //        .passwordEncoder(passwordEncoder()); // 使用加密验证
        auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService, new BCryptPasswordEncoder()));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/config/**", "/css/**", "/fonts/**", "/img/**", "/js/**","/v2/api-docs","/webjars/**","/swagger-resources/**","/swagger-ui.html",
                "/getCode","/checkCode");
    }
    /*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()
                .and().authorizeRequests()
                .antMatchers("/seller/registry").permitAll() // 注册请求不需要验证
                .antMatchers("/seller/sign_up").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/seller/sign_in")
                .loginProcessingUrl("/seller/login").defaultSuccessUrl("/seller/personal_center",true)
                .failureUrl("/seller/sign_in?error").permitAll()
                .and().logout().logoutSuccessUrl("/seller/sign_in").permitAll()
                .and().csrf().disable();
    }
*/

    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return handler;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        String[] methods = "POST,PUT,GET,DELETE,OPTIONS".split(",");
        String[] headers = "Origin,X-Requested-With,Content-Type,Accept,Accept-Encoding,Accept-Language,Host,Referer,Connection,User-Agent,Authorization".split(",");

        List<String> allowedOrigins = new ArrayList<>();
        List<String> allowedMethods = new ArrayList<>();
        List<String> allowedHeaders = new ArrayList<>();

        allowedOrigins.add("*");

        for(String s: methods) {
            allowedMethods.add(s);
        }
        for (String ss: headers) {
            allowedHeaders.add(ss);
        }

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setExposedHeaders(allowedHeaders);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/getCode").permitAll()
                    .antMatchers("/checkCode").permitAll()
                    .antMatchers("/user/registry").permitAll()
                    .antMatchers("/swagger-ui**").permitAll()
                    .antMatchers("/v2/api-docs").permitAll()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyRequest().authenticated()
                    //.anyRequest().permitAll()
                .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .sessionManagement().disable()
                    .cors()
                //.and()
                   // .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                   // new Header("Access-control-Allow-Origin","*"),
                   // new Header("Cache-Control", "no-store"),
                   // new Header("Access-Control-Expose-Headers","Authorization"))))
                .and()
                //    .formLogin().loginPage("/seller/login");
                    .logout()
                    .addLogoutHandler(tokenClearLogoutHandler())
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                    //.logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .and()
                    .addFilterAfter(new OptionsRequestFilter(), CorsFilter.class)
                //验证登陆
                    .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                //验证token
                    .addFilterBefore(new JWTAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    protected TokenClearLogoutHandler tokenClearLogoutHandler() {
        return new TokenClearLogoutHandler();
    }

}


