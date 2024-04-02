package com.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.text.ParseException;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${budgetplanner.jwtSecret}")
    private String jwtSecret;

    @Value("${budgetplanner.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            JWT jwt = JWTParser.parse(authToken);
            Date payload = jwt.getJWTClaimsSet().getExpirationTime();

            return System.currentTimeMillis() <= payload.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEmailFromToken(String token) {
        try {
            JWT jwt = JWTParser.parse(token);
            String payload = String.valueOf(jwt.getJWTClaimsSet());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload).get("eml");

            String email = jsonNode.asText();
            return email;
        } catch (ParseException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
