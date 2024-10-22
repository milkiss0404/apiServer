package org.zerock.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")
public class Member {

    @Id
    private String email;

    private String pw;

    private String nickname;

    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }
    public void clearRole(){
        memberRoleList.clear();
    }

    public void changePw(String Pw) {
        this.pw = Pw;
    }
    public void changeSocial(boolean Social) {
        this.social = Social;
    }


}
