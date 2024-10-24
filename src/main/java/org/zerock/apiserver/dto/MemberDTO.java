package org.zerock.apiserver.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

public class MemberDTO extends User {

    private String email,pw,nickname;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(String email, String pw, String nickname, boolean social, List<String> roleNames) {
        super(email,pw,roleNames.stream().map(str-> new SimpleGrantedAuthority("ROLE_"+ str)).collect(Collectors.toList()));
//SimpleGrantedAuthority 문자열로 권한을 만들어줌
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }
    // JWT 문자열의 내용을 Claims 라고함
    public Map<String,Object> getClaims() {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("email",email);
        dataMap.put("pw",pw); //이건뺴야함
        dataMap.put("nickname",nickname);
        dataMap.put("social",social);
        dataMap.put("roleNames",roleNames);
        return dataMap;
    }



}
