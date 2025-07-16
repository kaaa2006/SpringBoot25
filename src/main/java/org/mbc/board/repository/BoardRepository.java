package org.mbc.board.repository;

import org.mbc.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
// extends JpaRepository<Entity클래스,pk타입>
   //  JpaRepositorysms 는 jpa에서 미리 만들어 놓은 인터페이스로 crud와 페이징 처리 , 정렬등이 존재
}

