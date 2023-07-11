package com.carlosjr.am.users.security;

import com.carlosjr.am.users.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.trace("[ USER DETAILS SERVICE ] Try to found user in database.");
        CustomUserDetails customUserDetails =
                new CustomUserDetails(userService, username);
        log.trace("[ USER DETAILS SERVICE ] User found: " + customUserDetails.getUsername());
        return customUserDetails;
    }
}
