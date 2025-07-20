package com.example.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Base64;
import java.util.Date;

public class JWTUtil {
    private static final String SECRET = "secret_key";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);
    private static final long expiration_time = 15 * 60 * 1000; //15 minutes

    public static String generateToken(String username){
        return JWT.create().withSubject(username).withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() +expiration_time))
                .sign(algorithm);
    }

    public static String validateToken(String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }
}
