package org.mbc.board.security.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2     // custom403은 customsecurity 클래스에서 사용됨
public class Custom403Handler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.info("Custom403Handler.handle 메서드 실행 ");
        log.info("현재 사용자의 권한이 없거나 조건이 맞지 않습니다. ");
        log.info("403 예외발생중 ");

        response.setStatus(HttpStatus.FORBIDDEN.value());// 현재 예외상태값 저장

        String contentType = request.getHeader("content-type");  //json 여부

        boolean jsonRequest = contentType.contains("application/json");
        log.info("isJSON : " + jsonRequest);

        // 일반요청시
        if (!jsonRequest) {
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }
     }
    //AccessDeniedHandler 내장된 예외처리용 클래스 /재정의
    // 현재 사용자의 권환이 없거나
    // 특징 조건이 맞지 않아 예외가 발생하는 403 처리용 클래스
    // form 태그를 통해 전송된 결과를 처리 하거나
    // Axios를 이용해 비동기 처리 Ajax를 이용함으로 두가지 경우 다른 메세지 처리함

    // <form> 태그의 요청이 403 인 경우 로그인 페이지로 이동할때 ACCES_DENIED




    // Ajax 비동기 처리 json 처리




}
