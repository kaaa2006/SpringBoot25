package org.mbc.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder // 빌터패턴에는  ,Data AllArgsConstructor, NoArgsConstructor
@AllArgsConstructor //모든 필드를 생성자로 만듦
@NoArgsConstructor  // 기본 생성자
public class BoardDTO {

    private  Long bno;
    private  String title;
    private  String content;
    private  String writer;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
