package org.mbc.board.repository;

import org.mbc.board.domain.Board;
import org.mbc.board.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardSearch {   //BoardSearch 다중검색용 p451
// extends JpaRepository<Entity클래스,pk타입>
   //  JpaRepositorysms 는 jpa에서 미리 만들어 놓은 인터페이스로 crud와 페이징 처리 , 정렬등이 존재
    // 테스트 코드에서 jparepository 에 내장된 crud 와 페이징 정렬기법 테스트 함
    // Board result = boardrepository.save db에 insert 쿼리 동작을 함
    // Optional -> bno 값을 selet 함


    // 7.17 쿼리 메서드
    // 우리가 만드는 메서드 이름이 쿼리문이 된다.
    //https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    // 단점: 실제 사용시 상당히 길고 복잡한 메서드명이 된다 그래서 많이 사용 않함.
    Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);
    //인터페이스 구현 메서드 이므로 실행문이 없다.
    //findBy   Title Containing OrderBy BnoDesc
    // 찾는다   제목을             정렬   번호를 내림차순으로

    // @Query 쿼리메서드와 병합 JPQL
    @Query("select b from Board b where  b.title like concat('%',: keyword,'%') ")
    Page<Board> findKeyword(String keyword,Pageable pageable);
    //findKeyword 메서드가 실행되면 파라미터로 keyword를 받는다. 제목 검색 단어 where
    //쿼리문에 객체가 넘어가야 하므로 Board가 클래스 명이 되어야 한다.
    // select * from board where title like '%'keyword'% '
    //단점: join과 같은 복잡한 쿼리를 사용하지 못함
    // 원하는 속성들만 추출해서 객체처리하거나 DTO 처리가 안됨
    // 네이티브 쿼리 속성값을 true로 지정해서 특정 데이터베이스에 동작하는 sql 사용 안됨

    // 네이티브 쿼리 진짜 쿼리문을 사용하는 기법
    @Query(value = "select  now()",nativeQuery = true) // 진짜 쿼리문
    String getTime();
}

