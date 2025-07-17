package org.mbc.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.mbc.board.domain.Board;
import org.mbc.board.domain.QBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

//                                  쿼리dsl 상속                            인터페이스 지정
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     */
    public BoardSearchImpl() {  //생성자
        super(Board.class);
    }



    @Override // 인터페이스에서 만든 메서드 -> 실행코드 작성용
    public Page<Board> search1(Pageable pageable) {
        // querydsl로 다중검색용 코드 추가
        // 쿼리dsl 목적은 타입 기반으로 코드를 이용한다 -> Qdomain 클래스
        QBoard board= QBoard.board; // q도메인 객체

        JPQLQuery<Board> query = from(board); // select * from board

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // 다중조건일때 연산자 공식에 의해 특수기호가 먼저 계산 될 때가 있는데 괄호를 사용하면 먼저 보기 때문에 BooleanBuilder이 괄호 역할을 한다

       // query.where(board.title.contains("1")); // where title like 1 조건문이 만들어짐
        // select * from board where title like 1
        booleanBuilder.or(board.title.contains("11")); // where title like 11
        booleanBuilder.or(board.content.contains("11")); // where content like
        // ( where title like 11 or content like 11)

        query.where(booleanBuilder);  // ( where title like 11 or content like 11)
        query.where(board.bno.gt(0L));  //pk 를 이용해서 빠른 검색 where이 추가되면 and 조건
        //where (title like 11 or content like 11) and bno > 0
        //페이징 처리용 코드
        this.getQuerydsl().applyPagination(pageable,query);



        List<Board> list =query.fetch(); //쿼리문 실행 리스트에 담아라
        long count = query.fetchCount(); // r검색후 게시물 파악용

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        // 인터페이스에서 만든 추상메서드를 구현하는 클래스
        QBoard board = QBoard.board; // dsl 객체 생성
        JPQLQuery<Board> query = from(board); // select * from board
        //프론트에서 검색폼에 keyword 가 비어있을 경우도 있고 있을 경우도 있다
        if ((types != null && types.length > 0) && keyword != null) {

            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        //제목이면
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        //내용이면
                        break;
                    case "w":
                        //작성자면
                        booleanBuilder.or(board.title.contains(keyword));
                        break;

                }//프론트에서 넘어오는 String 배열값을 파악하고 적용

            }// for
            query.where(booleanBuilder); // 위에서 만든 조건을 적용하는 where title or content or writer
        }// if
       query.where(board.bno.gt(0L)); //pk이용해 인덱싱 처리용 코드
       // where (title or content or writer) and bno > 0L
        this.getQuerydsl().applyPagination(pageable,query);
        //page<T> 클래스는 3가지의 리턴타입을 만들어 준다.
        List<Board> list= query.fetch(); //쿼리문 실행

        long count = query.fetchCount(); // 검색된 게시물 수





        return new PageImpl<>(list,pageable,count);
        // 리턴 검색결과 board 페이징처리 검색된 갯수
    }

}