package org.mbc.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity // 테이블 관련 객체
@Getter
@Builder // 빌더 패턴 세터 대신 사용
@AllArgsConstructor //모든 필드값으로 생성자 만듬
@NoArgsConstructor  // 기본생성자
@ToString   // 객체 주소가 아닌 값을 출력하기 위해 사용
public class Board extends BaseEntity { //상속받고 날짜 관련된 jpa 연결

    @Id // pk선언  (notnull,unique,indexing)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 번호 생성
    private Long bno;        //번호
    @Column(length = 500, nullable = false) //nullable = false = nn
    private String title;    //제목
    @Column(length = 2000, nullable = false)
    private  String content; //내용
    @Column(length = 50, nullable = false)
    private String writer;   //작성자


    //Hibernate:
    //        create table board (
    //        bno bigint not null auto_increment,
    //        content varchar(255),
    //        title varchar(255),
    //        writer varchar(255),
    //        primary key (bno)
    //     )  engine=InnoDB
    public void  change(String title,String content){
        // 제목과 내용만 수정하는 메서드  (세터 대체용)
        this.title= title;
        this.content= content;

    }

}
