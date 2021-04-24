package com.jacky.register.security.filters;

import com.jacky.register.models.database.quetionail.collection.CollectionItemSelect;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.security.DatabaseEntityUserDetails;
import com.jacky.register.security.UserRole;
import com.jacky.register.security.authenticationToken.AdminAndDepartmentAuthenticationToken;
import org.hibernate.mapping.Collection;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
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
import java.util.Collections;
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

        if (auth == null)
            return;

        DatabaseEntityUserDetails<?> userDetails= (DatabaseEntityUserDetails<?>) auth.getPrincipal();



        var allRoles=auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.replace("ROLE_",""))
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
        }


        context.setAuthentication(auth);
        chain.doFilter(request, response);
    }
}
