package org.mbc.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    @GetMapping("/login")
    public void loginGET(String error, String logout) {
        //http://localhost/member/login?error=???
        // http://localhost/member/login?logout=???
        log.info("MemberController 메서드 실행...");
        log.info("에러" + error); //db에서 활용
        log.info("로그아웃" + logout); //db에서 활용

        if (logout != null) {
            // 로그아웃이 널이 아니면
            log.info("로그아웃 처리됨" + logout);
        }
    }


}
