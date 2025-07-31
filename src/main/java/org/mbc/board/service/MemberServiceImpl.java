package org.mbc.board.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Member;
import org.mbc.board.domain.MemberRole;
import org.mbc.board.dto.MemberJoinDTO;
import org.mbc.board.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor // final붙은 필드를 생성자로
public class MemberServiceImpl implements MemberService {
    //                          인터페이스로 만든 추상메서드를 구현 클래스로 제공

    private final ModelMapper modelMapper;  // 엔티티를 dto변환
    private final MemberRepository memberRepository;  // member db 처리용
    private final PasswordEncoder passwordEncoder;    // 패스워드 암호화


    @Override
    public MemberJoinDTO getMember(String mid) {
        Optional<Member> result = memberRepository.findById(mid);
        if (result.isPresent()) {
            Member member = result.get();
            return MemberJoinDTO.builder()
                    .mid(member.getMid())
                    .mpw(member.getMpw())
                    .email(member.getEmail())
                    .build();
        }
        return null;
    }

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {
        // 기존에 id가 있는지 확인
        String mid = memberJoinDTO.getMid(); // 프론트에서 id가 넘어옴
        boolean exist = memberRepository.existsById(mid); // 기존에 id 있는지 찾고 t/f


        if(exist) {
            throw new MidExistException(); // 중복id 처리용 예외처리 발생
        }
        // 진짜 회원가입처리
        Member member = modelMapper.map(memberJoinDTO, Member.class);
        // 엔티티                              dto

        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRole(MemberRole.USER);  // 일반회원으로

        log.info("=============================");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);

    }

    @Override
    public void modify(MemberJoinDTO memberJoinDTO) throws MidExistException {

        String mid = memberJoinDTO.getMid();

        if(mid == null){
            throw new MidExistException();
        }
        // 기존 회원 조회
        Member member = memberRepository.findById(mid).orElseThrow(MidExistException ::new);

        // 비밀번호 암호화
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));

        memberRepository.save(member);

        log.info("수정 완료: "+ member);
    }



}

