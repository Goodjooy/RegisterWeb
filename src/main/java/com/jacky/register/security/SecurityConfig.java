package com.jacky.register.security;

import com.jacky.register.security.filters.ExtraDateFilter;
import org.apache.http.impl.BHttpConnectionBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AdminUserDetailService adminUserDetailService;
    @Autowired
    SuperUserDetailService superUserDetailService;
    @Autowired
    PasswordEncoder encoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .csrfTokenRepository(new CookieCsrfTokenRepository())
                .ignoringAntMatchers("/api/csrf/token")
                .disable();

        http
                .authorizeRequests()
                .antMatchers("/api/question/**")
                .hasAnyRole(UserRole.ADMIN.getName(), UserRole.SUPER_ADMIN.getName())

                .antMatchers("/api/data/")
                .permitAll();

        http
                .formLogin()
                .loginProcessingUrl("/api/auth/login")
                .successForwardUrl("/api")
                .usernameParameter("uid")
                .passwordParameter("paswd");
       // http.addFilter(new ExtraDateFilter());

    }




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(adminUserDetailService).passwordEncoder(encoder)
                .and()
                .userDetailsService(superUserDetailService).passwordEncoder(encoder);


    }
}
