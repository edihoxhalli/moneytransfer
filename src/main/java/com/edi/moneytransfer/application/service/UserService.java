package com.edi.moneytransfer.application.service;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.persistence.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    void login(UserDto userDto);
    UserDetails loadUserByUsername(String username);
    User findByUsername(String username);
    Collection<? extends GrantedAuthority> getAuthoritiesFromContext();
    Collection<? extends GrantedAuthority> getAuthorities(User user);
    User getCurrentlyLoggedInUser();
}
