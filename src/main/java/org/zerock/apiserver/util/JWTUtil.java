package org.zerock.apiserver.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;
import javax.crypto.SecretKey;
@Log4j2
@Component
public class JWTUtil {
    @Value("${spring.jwt.key}")
    private String screctKey; // 인스턴스 변수로 설정

    private static String staticKey; // static 변수 선언

    @PostConstruct
    public void init() {
        staticKey = screctKey; // key 값을 static 변수에 할당
    }

    public static String generateToken(Map<String, Object> valueMap, int min) {
        SecretKey key = null;
        try{
            key = Keys.hmacShaKeyFor(JWTUtil.staticKey.getBytes("UTF-8"));
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ","JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();
        return jwtStr;
    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;
        try{
            SecretKey key = Keys.hmacShaKeyFor(JWTUtil.staticKey.getBytes("UTF-8"));
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();
        }catch(MalformedJwtException malformedJwtException){
            throw new CustomJWTException("MalFormed");
        }catch(ExpiredJwtException expiredJwtException){
            throw new CustomJWTException("Expired");
        }catch(InvalidClaimException invalidClaimException){
            throw new CustomJWTException("Invalid");
        }catch(JwtException jwtException){
            throw new CustomJWTException("JWTError");
        }catch(Exception e){
            throw new CustomJWTException("Error");
        }
        return claim;
    }
}