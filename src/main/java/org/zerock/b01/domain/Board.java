package org.zerock.b01.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

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

    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    //cascadeType.ALL : board객체의 모든 상태변화에 BoardImage객체 역시 같이 변경
    //하위 엔티티(BoardImage)의 상위객체(Board) 참조가 없는 상태가 되면 orphanRemoval = true 설정해줘야 하위엔티티 삭제가 됨
    //게시글 관점에서 다수의 첨부파일(HashSet자료구조 사용 - 무순서, null값 허용)
    //mappedBy : 매핑테이블(여기서는 board_image_set)을 생성하지 않기 위해 설정. 어떤 엔티티의 속성으로 매핑되는지를 설정(게시글을 연관관계의 주인으로 설정)
    @Builder.Default
    @BatchSize(size = 20) //쿼리 20개를 묶어서(Batch) 한번에 실행
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this) //현재 Board객체
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages(){

        imageSet.forEach(boardImage -> boardImage.changeBoard(null)); //Board객체에 등록되어있는 BoardImage객체들을 모두 제거

        this.imageSet.clear();
    }

}
