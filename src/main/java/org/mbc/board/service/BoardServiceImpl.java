package org.mbc.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Board;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service // 스프링에게 서비스 계층임을 알린다
@Log4j2
@RequiredArgsConstructor // 필드값을 보고 생성자를 만든다 final 필드나 @Nonnull이 붙은 필드용
@Transactional // commit 용 / 여러개의 테이블이 조합될때 해결 역할을 한다
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper; // 엔티티,DTO를 변환
    private  final BoardRepository boardRepository; //jpa 용 클래스 (C,R,U,D ,페이징 , 정렬, 다중검색 )

    @Override
    public Long register(BoardDTO boardDTO) { //조원이 실행코드 만든다.
       // 폼에서 넘어오면 dto 가 데이터베이스에 넘어감
        Board board = modelMapper.map(boardDTO,Board.class); // 엔티티가 dto 로 변환

        Long bno = boardRepository.save(board).getBno();
        //                          insert into board -> bno 로 받는다

        return bno; //프론트에 게시물 저장후 번호가 바뀐다 ,
    }

    @Override
    public BoardDTO readOne(Long bno) {
        Optional<Board> result= boardRepository.findById(bno);
        //select*from board  where bno= bno
        // Optional null이 나와도 예외처리 하지 않음
        Board board = result.orElseThrow(); //정상값이 나올때 엔티티로 받는다.

        BoardDTO boardDTO = modelMapper.map(board,BoardDTO.class);
        //모델매퍼를 이용해 엔티티로 나온 board를 dto로 변환한다.
        return boardDTO; //프론트를 dto 로 보낸다
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        // select*from board where bno=bno ->  엔티티로 나옴

        Board board = result.orElseThrow(); // 널이 아닐때 결과를 엔티티로 저장
        board.change(boardDTO.getTitle(),boardDTO.getContent());
        boardRepository.save(board);// db에 pk가 있으면 업데이트 , 없으면 인설트
    }

    @Override
    public void remove(Long bno) {
    boardRepository.deleteById(bno);
    // deledte from board where bno =bno
    }


}
