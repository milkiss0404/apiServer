package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.apiserver.util.CustomJWTException;
import org.zerock.apiserver.util.JWTUtil;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {

    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader,
                                       String refreshToken){
        if(refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }
        if(authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }
        String accessToken = authHeader.substring(7);
            //Access 토큰이 만료되지 않았다면
        if(!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }
        //여기로 나오면 accessToken 은 당연히 만료된거임

            //Refresh토큰 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("refresh ... claims: " + claims);
        String newAccessToken = JWTUtil.generateToken(claims, 10);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) ? JWTUtil.generateToken(claims, 60*24) : refreshToken;
        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }


    private boolean checkTime(Integer exp) {
        //JWT exp 를 날짜로변환
        Date date = new Date((long) exp * (1000));

        //현재 시간과의 차이 계산 - 밀리세컨
        long gap = date.getTime() - System.currentTimeMillis();

        //분단위 계산
        long leftMin = gap / (1000 * 60);

        //1시간도 안남았는지 확인
        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);
        }catch (CustomJWTException ex){
            if (ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }

}
