package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.*;

@Entity //엔티티 객체는 반드시 @Entity 를 적용해야 함
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity{

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //키 생성전략 - identity : auto increment
    private Long bno;

    @Column(length = 500, nullable = false) //not null
    private String title;

    @Column(length = 2000, nullable = false) //not null
    private String content;

    @Column(length = 50, nullable = false) //not null
    private String writer;

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }
}
