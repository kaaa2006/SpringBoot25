package org.mbc.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.domain.Board;
import org.mbc.board.dto.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {
@Autowired
    private BoardService boardService ;
@Test
    public void testRegister(){

    log.info("등록용 테스트 서비스 실행중.....");
    log.info(boardService.getClass().getName());

    BoardDTO boardDTO = BoardDTO.builder() // 세터 대신 builder
            .title("서비스에서 만든 제목 ")
            .content("서비스에서 만든 내용")
            .writer("서비스님")
            .build();

    Long bno = boardService.register(boardDTO);
    log.info("테스트 결과 bno" + bno);
//Hibernate:
//    insert
//    into
//        board
//        (content, moddate, regdate, title, writer)
//    values
//        (?, ?, ?, ?, ?)
}
@Test
    public void testModify(){
    BoardDTO boardDTO = BoardDTO.builder()
            .bno(201L)
            .title("수정된제목")
            .content("수정된내용")
            .build();
        boardService.modify(boardDTO);
}
}
