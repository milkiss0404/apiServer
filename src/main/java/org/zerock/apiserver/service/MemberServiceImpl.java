package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.domain.MemberRole;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.repository.MemberRepository;

import java.security.UnrecoverableEntryException;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        //access Token 을 이용해서 사용자 정보 가져오기 ===닉네임을 가져왔음
        String nickname = getEmailFromKakaoAccessTokoen(accessToken);


        Optional<Member> result = memberRepository.findById(nickname);

        if(result.isPresent()){
          MemberDTO memberDTO =   entityToDTO(result.get());
            return memberDTO;
        }

        Member socialMember = makeSocialMember(nickname);

        memberRepository.save(socialMember);

        MemberDTO memberDTO = entityToDTO(socialMember);

        //기존 DB에 회원정보가 있는경우 or 없는경우
        return memberDTO;
    }

    private String getEmailFromKakaoAccessTokoen(String accessToken) {

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization","Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, entity, LinkedHashMap.class);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        LinkedHashMap<String, String> kakaoAcount = bodyMap.get("properties");

        log.info(kakaoAcount);
        String nickname = kakaoAcount.get("nickname");
        log.info(nickname);
        return nickname;
    }



    private Member makeSocialMember(String nickname) {
        String tempPassword = makeTempPassword(); //비밀번호 생성 -> 추후생성(유효성검사 도 같이)

        log.info("tempPassword: " +tempPassword);

        Member member = Member.builder()
                .email(nickname)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname("Social Member")
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;
    }

    private String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 10; i++) {
            buffer.append((char) (int) (Math.random() * 55) + 65);
        }
        return buffer.toString();
    }
}
