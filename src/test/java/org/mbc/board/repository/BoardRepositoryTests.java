package org.mbc.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests { //영속성 계층 테스트용
    @Autowired // 생성자 자동 주입 
    private BoardRepository boardRepository;
    @Test
    public void  testInsert(){
    IntStream.rangeClosed(1,100).forEach(i -> {
        // i변수에 1~99 까지 100개의 정수를 반복해서 생성
                Board board = Board.builder()
                        .title("제목..." +i)   //board.setTitle()
                        .content("내용..." +i)//board.setContent()
                        .writer("user..." +(i%10)) //board.setWriter()
                        .build(); //@Builder용 (세터 대신 간단하고 가독성 좋게)

                /*log.info(board);*/
        Board result = boardRepository.save(board); // 데이터베이스에 기록하는 코드
        // .save 메서드는 jpa에서 상속한 메서드로 값을 저장하는 용도로 사용함
        // 이때 이미 값이 있으면 update를 진행한다.
        log.info("게시물 번호" + result.getBno() +"게시물 제목" + result.getTitle() );

    }   // foreach문 종료
    );  //Inseram 종료
}   //testInsert 메서드 종료
    @Test
    public void  testSelect(){
        Long bno = 100L;    // 번호가 100인 개체를 확인해보자

       Optional<Board> result = boardRepository.findById(bno); // select * from board where bno = bno;
        // 널값이 나올 경우를 대비한 객체
        // Hibernate:
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer
        //    from
        //        board b1_0
        //    where
        //        b1_0.bno=?
       Board board = result.orElseThrow(); // 값이 있으면 넣어라

        log.info(bno + "가 데이터베이스에 존재합니다.");
        log.info(board); // Board(bno=100, title=제목...100, content=내용...100, writer=user...0)

    } // testSelect 메서드 종료
    @Test
    public void testUpdate (){
        Long bno = 100L;

       Optional<Board> result= boardRepository.findById(bno); // bno를 찾아서 result에 넣는다.

       Board board = result.orElseThrow(); // 가져온 값이 있으면 board 타입에 객체를 넣음
       board.change("수정테스트 제목","수정테스트 내용");// 제목과 내용만 수정 할 수 있는 메서드

        boardRepository.save(board); // .save 메서드는 pk값이 없으면 insert, pk값이 있으면 update한다.
    //Hibernate:
        //    select
        //        b1_0.bno,
        //        b1_0.content,
        //        b1_0.moddate,
        //        b1_0.regdate,
        //        b1_0.title,
        //        b1_0.writer
        //    from
        //        board b1_0
        //    where
        //        b1_0.bno=?
    }
@Test
    public void testDelete(){

        Long bno=1L;
        boardRepository.deleteById(bno); // deleteById -> delete from board where bno=bno
    //Hibernate:
    //    select
    //        b1_0.bno,
    //        b1_0.content,
    //        b1_0.moddate,
    //        b1_0.regdate,
    //        b1_0.title,
    //        b1_0.writer
    //    from
    //        board b1_0
    //    where
    //        b1_0.bno=?
    //Hibernate:
    //    delete
    //    from
    //        board
    //    where
    //        bno=?

}
@Test
    public void testPaging(){
    // .findAll()는 모든 리스트를 출력하는 메서드이다 (select * from board;)
    // 전체 리스트에 페이징과 정렬 기법도 추가해보자 
    Pageable pageable = PageRequest.of(0,10 ,Sort.by("bno").descending());
    //                                  시작번호 10개 ,page당 데이터 수
    //                                                                  번호를 기주느로 내림차순 정렬



    //Hibernate:
    //    select
    //        b1_0.bno,
    //        b1_0.content,
    //        b1_0.moddate,
    //        b1_0.regdate,
    //        b1_0.title,
    //        b1_0.writer
    //    from
    //        board b1_0
    //    order by
    //        b1_0.bno desc (bno를 기준으로 내림차순 정렬)
    //    limit
    //        ?, ? ( 시작번호 , 끝번호 )
    //Hibernate:
    //    select
    //        count(b1_0.bno) board 전체 리스트 수를 알아옴
    //    from
    //        board b1_0
    Page<Board> result= boardRepository.findAll(pageable);
    // 1장 종이에 board 객체를 가지고 있는 결과는 result에 담는다
    // page 클래스는 다음 페이지 존재 여부 , 이전 페이지 존재여부 전체 데이터 개수 등등을 계산한다
    log.info("전체 게시물 수 :" + result.getTotalElements()); //99
    log.info("총 페이지 수 :" + result.getTotalPages()); //10
    log.info("현재 페이지 번호 :" +result.getNumber());    //0
    log.info("페이지당 페이지 개수 :" +result.getSize());    //10
    log.info("다음 페이지 여부 :" +result.hasNext());  //true
    log.info("시작 페이지 여부 :" +result.isFirst());  //true

    List<Board> boardList = result.getContent(); //페이징 처리된 내용을 가져와라

    boardList.forEach(board -> log.info(board)); // forEach는 인덱스 사용하지 않음 앞에서 객체를 리턴한다.
    // (board -> log.info(board))( -> ): 람다식 1개의 명령어가 있을 때 활용

    }
@Test
    public void testsearch1(){
        Pageable pageable = PageRequest.of(1,10, Sort.by("bno").descending());
       Page<Board> result = boardRepository.search1(pageable);
    result.getContent().forEach(board -> log.info(board));
    //Hibernate:
    //    select
    //        b1_0.bno,
    //        b1_0.content,
    //        b1_0.moddate,
    //        b1_0.regdate,
    //        b1_0.title,
    //        b1_0.writer
    //    from
    //        board b1_0
    //    where
    //        b1_0.title like ? escape '!' -> like 1
//Hibernate:
//    select
//        b1_0.bno,
//        b1_0.content,
//        b1_0.moddate,
//        b1_0.regdate,
//        b1_0.title,
//        b1_0.writer
//    from
//        board b1_0
//    where
//        (
//            b1_0.title like ? escape '!'
//            or b1_0.content like ? escape '!' -> 조건이 2개 title,content
//        )
//        and b1_0.bno>?
//    order by
//        b1_0.bno desc
//    limit
//        ?, ? this.getQuerydsl().applyPagination(pageable,query);
//Hibernate:
//    select
//        count(b1_0.bno)
//    from
//        board b1_0
//    where
//        (
//            b1_0.title like ? escape '!'
//            or b1_0.content like ? escape '!'
//        )
//        and b1_0.bno>?
    }
@Test
    public void testSearchAll(){
    // 프론트에서 t가 선택되면  title, c 가 선택되면 content , w: writet 조건으로 제시됨

    String[] types = {"t","c","w"};
    String keyword = "1"; // 검색 단어

    Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

    Page<Board> result = boardRepository.searchAll(types,keyword,pageable);
    //Hibernate:
    //    select
    //        b1_0.bno,
    //        b1_0.content,
    //        b1_0.moddate,
    //        b1_0.regdate,
    //        b1_0.title,
    //        b1_0.writer
    //    from
    //        board b1_0
    //    where
    //        (
    //            b1_0.title like ? escape '!'
    //            or b1_0.content like ? escape '!'
    //            or b1_0.title like ? escape '!'
    //        )
    //        and b1_0.bno>?
    //    order by
    //        b1_0.bno desc
    //    limit
    //        ?, ?
    //Hibernate:
    //    select
    //        count(b1_0.bno)
    //    from
    //        board b1_0
    //    where
    //        (
    //            b1_0.title like ? escape '!'
    //            or b1_0.content like ? escape '!'
    //            or b1_0.title like ? escape '!'
    //        )
    //        and b1_0.bno>?
   //Hibernate:
    //    select
    //        b1_0.bno,
    //        b1_0.content,
    //        b1_0.moddate,
    //        b1_0.regdate,
    //        b1_0.title,
    //        b1_0.writer
    //    from
    //        board b1_0
    //    where
    //        (
    //            b1_0.title like ? escape '!'
    //            or b1_0.content like ? escape '!'
    //            or b1_0.title like ? escape '!'
    //        )
    //        and b1_0.bno>?
    //    order by
    //        b1_0.bno desc
    //    limit
    //        ?, ?
    //Hibernate:
    //    select
    //        count(b1_0.bno)
    //    from
    //        board b1_0
    //    where
    //        (
    //            b1_0.title like ? escape '!'
    //            or b1_0.content like ? escape '!'
    //            or b1_0.title like ? escape '!'
    //        ) booleanbuilder 가 괄호 쳐 줌
    //        and b1_0.bno>?
    log.info("전체 게시물 수 :" + result.getTotalElements()); //99
    log.info("총 페이지 수 :" + result.getTotalPages()); //10
    log.info("현재 페이지 번호 :" +result.getNumber());    //0
    log.info("페이지당 페이지 개수 :" +result.getSize());    //10
    log.info("다음 페이지 여부 :" +result.hasNext());  //true
    log.info("시작 페이지 여부 :" +result.isFirst());  //true

    result.getContent().forEach(board -> log.info(board));
    }


}// 클래스 종료
