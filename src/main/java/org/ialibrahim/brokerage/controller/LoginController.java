package org.ialibrahim.brokerage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ialibrahim.brokerage.security.JwtTokenUtil;
import org.ialibrahim.brokerage.security.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public ResponseEntity<Void> login() throws JsonProcessingException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwtTokenUtil.createToken(user);
        return ResponseEntity.ok().headers(buildResponseHeader(token)).body(null);
    }

    private HttpHeaders buildResponseHeader(String token) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.AUTHORIZATION, token);

        return responseHeaders;
    }
}
