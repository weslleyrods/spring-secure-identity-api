package com.weslley.demo_crud.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.weslley.demo_crud.model.UserModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;


@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserModel user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
        .withIssuer("demo-crud-api")
        .withSubject(user.getEmail())
        .withExpiresAt(genExpirationDate())
        .sign(algorithm);

        return token;
    }

    public String validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("demo-crud-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (Exception e){
            return e.getMessage();
        }
    }


    private Instant genExpirationDate() {
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }
}