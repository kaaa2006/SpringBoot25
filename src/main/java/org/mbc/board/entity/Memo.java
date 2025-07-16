package org.mbc.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // 데이터베이스 관련 객체임을 선언
@Table(name="tbl_memo") // 데이터베이스 테이블명 선언
@ToString
@Getter
@Builder    // 빌더패턴 사용 member.setName() 에서 member.name  가능하게 해줌
@AllArgsConstructor // 모든 필드값을 이용해 생성자 만듦
@NoArgsConstructor // 기본생성자

public class Memo {
    @Id // pk선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // GenerationType.IDENTITY -> pk를 자동으로 생성하고자 함.(키생성전략)
    // 만일 연결되는 데이터베이스가 오라클이면 번호를 위한 별되의 테이블 생성(시퀀스 객체)
    // myspl이나 Maria 면 auto increment를 기본으로 사용해서 새로운 레코드가 기록될때 다른 번호를 준다.
    // GenerationType.auto -> jpa가 알아서 생성하는 방식으로 결정
    // GenerationType.sequence -> 데이터베이스가 시퀀스를 이용해서 키를 생성
    // GenerationType.table -> 키 생성 전용 테이블을 생성해서 키를 생성


    private Long mno;

    @Column(length = 200, nullable = false) //200글자에 notnull
    private String memoText;


}
