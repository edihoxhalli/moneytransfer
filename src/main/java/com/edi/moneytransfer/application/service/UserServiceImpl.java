package com.edi.moneytransfer.application.service;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.application.mapper.UserMapper;
import com.edi.moneytransfer.persistence.entity.Account;
import com.edi.moneytransfer.persistence.entity.Authority;
import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        user.setAccount(account);
        account.setUser(user);
        Authority authority = new Authority();
        authority.setAuthority("USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        user.setAuthorities(authorities);
        return userMapper.toDto(userRepository.save(user));
    }



    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Username Not Found");

        UserDetails springUser =
                org.springframework.security.core.userdetails.User.builder()
                        .authorities(this.getAuthorities(user))
                        .username(username)
                        .password(user.getPassword())
                        .build();

        return springUser;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Collection<? extends GrantedAuthority> getAuthoritiesFromContext(){
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities;
    }

    @Override
    public User getCurrentlyLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(User user){
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getAuthorities().forEach(authority ->
                authorities.add(new SimpleGrantedAuthority(authority.getAuthority())));
        return authorities;
    }

    public void login(UserDto userDTO){
        UserDetails userDetails = this.loadUserByUsername(userDTO.getUsername());
        if(bCryptPasswordEncoder.matches(userDTO.getPassword(), userDetails.getPassword())) {

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                            userDTO.getPassword(), userDetails.getAuthorities());

            authentication = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);


        } else {
            throw new BadCredentialsException("Username and password do not match!");
        }
    }
}
