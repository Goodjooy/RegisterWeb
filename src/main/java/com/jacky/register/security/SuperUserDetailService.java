package com.jacky.register.security;

import com.jacky.register.models.database.users.SuperUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SuperUserDetailService implements UserDetailsService {
    @Autowired
    SuperUserRepository repository;

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
        var builder = User.withUsername(username);
        var result = repository.findByEmail(username);

        if (result.isPresent()) {
            var admin = result.get();

            builder.roles(UserRole.SUPER_ADMIN.getName());
            builder.password(admin.password);

            return builder.build();
        }
        throw new UsernameNotFoundException("admin Not Found");
    }

}
