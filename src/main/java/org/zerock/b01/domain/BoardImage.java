package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage> {

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne //첨부파일 관점에서 게시글을 바라봄
    private Board board;

    @Override //Comparable : OneToMany 처리에서 순번에 맞게 정렬하기 위함
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }

    private void changeBoard(Board board){ //board객체를 나중에 지정하게(바꾸게) 하는 메서드 - 게시글 삭제 시 BoardImage 객체의 게시글 참조 변경 위함
        this.board = board;
    }
}
