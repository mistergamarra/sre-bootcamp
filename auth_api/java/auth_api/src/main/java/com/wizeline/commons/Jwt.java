package com.wizeline.commons;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

public class Jwt {



    public static String createJWT(String username, String role) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Constants.SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .claim("role",role)
               // .signWith(signatureAlgorithm,signingKey);
        .signWith(SignatureAlgorithm.HS256, Constants.SECRET_KEY);

        builder.setHeaderParam("typ","JWT");
        return builder.compact();


    }

    public static Claims decodeJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(Constants.SECRET_KEY))
                .parseClaimsJws(jwt)
                .getBody();
    }

}