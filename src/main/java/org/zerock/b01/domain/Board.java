package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity //엔티티 객체는 반드시 @Entity 를 적용해야 함
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
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

    @OneToMany(mappedBy = "board") //게시글 관점에서 다수의 첨부파일(HashSet자료구조 사용 - 무순서, null값 허용)
    //mappedBy : 매핑테이블(여기서는 board_image_set)을 생성하지 않기 위해 설정. 어떤 엔티티의 속성으로 매핑되는지를 설정(게시글을 연관관계의 주인으로 설정)
    @Builder.Default
    private Set<BoardImage> imageSet = new HashSet<>();
}
