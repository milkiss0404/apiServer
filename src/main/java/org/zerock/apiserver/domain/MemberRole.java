package org.zerock.apiserver.domain;

import jakarta.persistence.*;

public enum MemberRole {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email") // 외래 키 설정
    private Member member;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private
}
