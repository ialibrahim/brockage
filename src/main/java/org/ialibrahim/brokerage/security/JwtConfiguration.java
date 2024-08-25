package org.ialibrahim.brokerage.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfiguration {

    //TODO: add to properties
    private String secretKey = "SECRETKEY";
    private String issuer = "me";
    private Integer accessTokenValidity = 30;
    private String accessTokenHeader="Authorization";
    private String accessTokenPrefix="Bearer";

    @Bean
    public Algorithm algorithm() {

        return Algorithm.HMAC256(secretKey);
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {

        return JWT.require(algorithm).withIssuer(issuer).build();
    }
}
