package com.jacky.register.security;

import com.jacky.register.models.database.users.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailService implements UserDetailsService {
    @Autowired
    AdminRepository repository;
    @Autowired
    PasswordEncoder encoder;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userInfo=username.split(";");
        // no extra info -> admin
        if(userInfo.length<2)
            username=userInfo[0];
        else {
            if (UserRole.SUPER_ADMIN.getName().equalsIgnoreCase(userInfo[1])){
                throw new UsernameNotFoundException("Super User Mode");
            }else {
                username=userInfo[0];
            }
        }

        var builder = User.withUsername(username);
        var result = repository.findByEmail(username);
        if (result.isPresent()) {
            var admin = result.get();
            var groupIn=admin.groupIn;

            builder.roles(UserRole.ADMIN.getName());
            builder.password(admin.password);

            return DatabaseEntityUserDetails.fromUserDetails(builder.build(), admin,admin.groupIn.ID);
        }
        throw new UsernameNotFoundException("admin Not Found");
    }
}
