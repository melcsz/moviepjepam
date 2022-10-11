package com.example.moviepj.controller;


import com.example.moviepj.exception.EmailOrPasswordDoNotMatch;
import com.example.moviepj.persistance.entity.status.UserStatus;
import com.example.moviepj.security.JwtRequest;
import com.example.moviepj.security.JwtResponse;
import com.example.moviepj.security.JwtTokUtil;
import com.example.moviepj.security.JwtUserDetailService;
import com.example.moviepj.service.UserService;
import com.sun.media.jfxmedia.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokUtil jwtTokUtil;
    private final JwtUserDetailService userDetailsService;
    private final UserService userService;

    public JwtAuthenticationController(AuthenticationManager authenticationManager,
                                       JwtTokUtil jwtTokUtil,
                                       JwtUserDetailService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokUtil = jwtTokUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @ModelAttribute("user") JwtRequest authenticationRequest) throws Exception {

        if(userService.getByEmail(authenticationRequest.getUsername()).getStatus().equals(UserStatus.NOT_ACTIVATED)){
            throw new DisabledException("account not activated");
        }
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new EmailOrPasswordDoNotMatch();
        }
    }
}