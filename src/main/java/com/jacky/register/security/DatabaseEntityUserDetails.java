package com.jacky.register.security;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class DatabaseEntityUserDetails<MODEL> extends User {
    private final MODEL model;
    private int departmentId;

    /**
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     *
     * @param username 用户名称
     * @param password  用户密码
     * @param authorities 用户权限
     */
     DatabaseEntityUserDetails(String username,
                                     String password,
                                     Collection<? extends GrantedAuthority> authorities,
                                     MODEL model,int departmentId) {
        super(username, password, authorities);
        this.model = model;
         this.departmentId = departmentId;
     }

    /**
     * Construct the <code>User</code> with the details required by
     * {@link DaoAuthenticationProvider}.
     *
     * @param username              the username presented to the
     *                              <code>DaoAuthenticationProvider</code>
     * @param password              the password that should be presented to the
     *                              <code>DaoAuthenticationProvider</code>
     * @param enabled               set to <code>true</code> if the user is enabled
     * @param accountNonExpired     set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not
     *                              expired
     * @param accountNonLocked      set to <code>true</code> if the account is not locked
     * @param authorities           the authorities that should be granted to the caller if they
     *                              presented the correct username and password and the user is enabled. Not null.
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as
     *                                  a parameter or as an element in the <code>GrantedAuthority</code> collection
     */
    DatabaseEntityUserDetails(String username,
                                     String password,
                                     boolean enabled,
                                     boolean accountNonExpired,
                                     boolean credentialsNonExpired,
                                     boolean accountNonLocked,
                                     Collection<? extends GrantedAuthority> authorities,
                                     MODEL model,int departmentId ) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.model = model;
        this.departmentId = departmentId;
    }


    static public <MODEL> DatabaseEntityUserDetails<MODEL> fromUserDetails(UserDetails userDetails, MODEL model,int departmentId) {
        return new DatabaseEntityUserDetails<MODEL>(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.isEnabled(),
                userDetails.isAccountNonExpired(),
                userDetails.isCredentialsNonExpired(),
                userDetails.isAccountNonLocked(),
                userDetails.getAuthorities(),
                model,departmentId);
    }
    static public <MODEL> DatabaseEntityUserDetails<MODEL> fromUserDetails(UserDetails userDetails, MODEL model){
        return fromUserDetails(userDetails,model,-1);
    }
    public MODEL getModel() {
        return model;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
