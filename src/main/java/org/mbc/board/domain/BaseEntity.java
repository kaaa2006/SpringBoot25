package org.mbc.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 공통적인 최상위 클래스
@EntityListeners(value = {AuditingEntityListener.class}) //감시용 클래스 명시
@Getter // 날짜용 처리만 sysdate 데이터베이스 날짜를 가져오기만 하겠다 (보안)
abstract class BaseEntity {  //abstract 추상적인 : 자체 실행은 안됨 이용해서 써먹음
//모든 테이블에 공통적으로 사용되는 필드를 만든다. 날짜,수정날짜


    @CreatedDate //생성일용 
    @Column(name="regdate",updatable = false)  //updatable = false 수정금지
    private LocalDateTime regDate;  // 등록일

    @LastModifiedDate // 수정일용
    @Column(name = "moddate") //DB 필드명 지정
    private LocalDateTime modDate;  // 수정일


}
