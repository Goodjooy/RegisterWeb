package com.jacky.register.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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
                .ignoringAntMatchers("/api/csrf/token","/api/question/collection/**","/api/examCycle/collection/**");
                //.disable();

        http
                .authorizeRequests()
                .antMatchers("/api/question/**","/api/examCycle/**")
                .hasAnyRole(UserRole.ADMIN.getName(), UserRole.SUPER_ADMIN.getName())

                .antMatchers("/api/question/collection/**","/api/examCycle/collection/**")
                .permitAll();

        http
                .formLogin()
                .loginProcessingUrl("/api/auth/login")
                .successForwardUrl("/api")
                .usernameParameter("uid")
                .passwordParameter("paswd");
        // http.addFilter(new ExtraDateFilter());

        http
                .headers()
                .xssProtection()
                .and()

                .contentSecurityPolicy("script-src 'self'");

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(adminUserDetailService).passwordEncoder(encoder)
                .and()
                .userDetailsService(superUserDetailService).passwordEncoder(encoder);


    }
}
