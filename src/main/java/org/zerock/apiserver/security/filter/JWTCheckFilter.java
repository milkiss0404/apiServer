package org.zerock.apiserver.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {  // OncePerRequestFilter 모든 요청에 한번은 거친다
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();
        log.info("check uri --------------------------------------------"+path);

        //false == check true == no check
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        //인코딩해서 포장한 인증 정보를 Authorization 헤더에 실어서 서버에 전송한다.
        //Baarer
        log.info("----------------------------------------------------");
        log.info("----------------------------------------------------");
        log.info("----------------------------------------------------");

        filterChain.doFilter(request,response);
    }
}
