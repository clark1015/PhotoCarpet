package com.koss.photocarpet.Security;

import com.koss.photocarpet.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5";
    public String create(User user) {
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));
        /*

    { // header "aIg": "HS512"
}.
    { / / payLoad
    " sub" : "4028809)784915d20L784976a40c000t",
    "iss": "demo app",
    "iat" :1595733657,
    "exp" :1596597657
         */
        // SECRET_KEY 를 이용해 서명한 부분
        return Jwts.builder()
                // header 에 들어갈 내용 및 서명을 하기 위한 시크릿키
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(String.valueOf(user.getUserId()))
                .setIssuer("demo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }
    public String validateAndGetUserId(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    }

