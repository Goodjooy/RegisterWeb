package com.jacky.register.security.filters;

import com.jacky.register.security.DatabaseEntityUserDetails;
import com.jacky.register.security.UserRole;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExtraDateFilter extends HttpFilter {
    final static String departmentIdName = "DPMIN_INDEX_VAL";

    @Override

    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {


        var context = SecurityContextHolder.getContext();
        var auth = context.getAuthentication();

        if (auth == null) {
            chain.doFilter(request, response);
            return;


        }
        DatabaseEntityUserDetails<?> userDetails;
        if (auth.getPrincipal() instanceof DatabaseEntityUserDetails)
            userDetails = (DatabaseEntityUserDetails<?>) auth.getPrincipal();
        else {
            auth.setAuthenticated(false);
            chain.doFilter(request, response);
            return;
        }


        var allRoles = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.toList());

        if (allRoles.contains(UserRole.SUPER_ADMIN.getName())) {

            var cookies = request.getCookies();
            Cookie cookieData;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(departmentIdName)) {
                    cookieData = cookie;
                    userDetails.setDepartmentId((int) Long.parseLong(cookieData.getValue()));
                    break;
                }
            }
            context.setAuthentication(auth);
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);

    }
}
