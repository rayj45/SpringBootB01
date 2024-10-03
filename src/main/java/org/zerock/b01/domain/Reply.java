package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Reply", indexes = {
    @Index(name = "idx_reply_board_bno", columnList = "board_bno") //board테이블과의 fk index설정
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board") //ToString시 참조하는 객체는 제외해야함 -
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY) //다대일 관계설정 - 연관관계 설정시 지연로딩(필요한 순간까지 DB와 연결하지 않는 방식)
    private Board board; //회원과 팀 예시에서 회원에 해당

    private String replyText;

    private String replyer;

}
