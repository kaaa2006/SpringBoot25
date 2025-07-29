package org.mbc.board.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service    //서비스계층 명시
//@RequiredArgsConstructor// final 필드에 대한 생성자 p689제외
public class CustomUserDetailsService implements UserDetailsService {
    // UserDetailsService 인터페이스를 구현하는 클래스
    // UserDetailsService 단하나의 메서드를 가지고 동작함
    // 구현클래스 loadUserByUsername을 재정의해서 사용한다.

    // pw를 암호화 처리 하도록 CustomSecurityConfig에 구현
    private PasswordEncoder passwordEncoder;   //new BCryptPasswordEncoder();

    // 기본생성자
    public CustomUserDetailsService() {
        // CustomUserDetailsService 호출되면 자동으로 pw를 암호화 처리가능 하게
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 실제 인증처리 할 떄 호출되는 메서드
        // 프론트에서 id로 넘어오는 username값을 처리한다.
        log.info("CustomUserDetailsService.loadUserByUsername 메서드 호출 ...");
        log.info("loadUserByUsername.로그온 사용자의 이름 :" + username); // 이 메서드가 호출되면 사용자 명이 넘어와 처리한다. (email,id,학번 등..)

        // 이 메서드의 리턴 타입은 UserDetails 라는 인터페이스 타입이다
        // UserDetails 는 사용자 인증 (Authentication) 과 관련된 정보들을 저장하는 역할
        // 스프링 시큐리티는 내부적으로 UserDetails 타입객체를 이용해서 패스워드를 검사하고 사용자 권한을 확인하는 방식으로 동작한다.
        // https://raccon.tistory.com/45  유저디테일 정리 링크


        UserDetails userDetails = User.builder() // User객체는 사용자 객체
                .username("USER1")               // User 객체의 id
                .password(passwordEncoder.encode("1111"))                // User 객체의 pw
                .authorities("ROLE_USER") // 일반사용자 권한
                .build();  // 빌더패턴 끝


        return userDetails;
    }
}
