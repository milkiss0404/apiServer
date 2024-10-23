package org.zerock.apiserver.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.dto.MemberDTO;
import org.zerock.apiserver.repository.MemberRepository;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailService implements UserDetailsService {  ////로그인을 처리할떄 동작하겠죠?

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.getWithRoles(username);

        if (member == null) {
            throw new UsernameNotFoundException("not found");
        }
        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList())

        );


        return memberDTO;
    }
}
