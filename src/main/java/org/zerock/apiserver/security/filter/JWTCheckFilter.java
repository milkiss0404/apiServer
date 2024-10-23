package org.zerock.apiserver.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.util.JWTUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {  // OncePerRequestFilter 모든 요청에 한번은 거친다
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();
        log.info("check uri --------------------------------------------"+path);

        if(path.startsWith("/api/member/")){
            return true;
        }

        //false == check true == no check
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        //인코딩해서 포장한 인증 정보를 Authorization 헤더에 실어서 서버에 전송한다.
        //Baarer

        try {
            String authHeaderStr = request.getHeader("Authorization");

            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);
            // 시큐리티 컨텍스트 홀더 에다가 이사용자의 정보를 넣어줘야함

            String email = (String)claims.get("email");
            String pw = (String)claims.get("pw");
            String nickname = (String)claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String>roleNames = (List<String>)claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames);

            log.info("--------------------------");
            log.info(memberDTO.getAuthorities());
            log.info("--------------------------");


            //스프링 시큐리티가 사용하는토큰임
            //상태가 없기떄문에 매번 토큰이 호출될떄마다 시큐리티 컨텍스트홀더에 넣어주고 권한 확인할수있음
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberDTO,pw,memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);


        }catch (Exception e) {
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.println(msg);
            writer.close();
        }
    }
}
