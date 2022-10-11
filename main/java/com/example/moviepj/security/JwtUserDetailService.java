package com.example.moviepj.security;

import com.example.moviepj.persistance.entity.UserEntity;

import com.example.moviepj.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class JwtUserDetailService implements UserDetailsService {

    public static final String SESSION_USER_KEY = "SESSION_USER";

    final UserRepository userRepository;

    @Autowired
    public JwtUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = getUserEntityByUsername(username.toLowerCase()).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with name=%s was not found", username)));
        storeSessionUser(user);
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new User(username, user.getPassword(), authorities);
    }

    private void storeSessionUser(UserEntity user) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        servletRequestAttributes.getRequest().getSession().setAttribute(SESSION_USER_KEY, user);
    }

    private Optional<UserEntity> getUserEntityByUsername(String username) {
        UserEntity user = userRepository.getByEmail(username);
        return Optional.ofNullable(user);
    }
}