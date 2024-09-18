package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.domain.Board;

import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(i -> {
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("user" + (i % 10)) //i를 10으로 나눈 나머지(1,2,...9,0,1,2..)
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO" + result.getBno());
        });
    }

    @Test
    public void testSelect(){
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow(); //orElseThrow : Optional의 예외처리

        log.info(board);
    }

    @Test
    public void testUpdate(){
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow(); //예외처리 던지고

        board.change("update...title100", "update...content100"); //board의 change메서드로 board객체 내용을 변경시키고

        boardRepository.save(board);
    }

    @Test
    public void testDelete(){
        Long bno = 1L;

        boardRepository.deleteById(bno);
    }

}
