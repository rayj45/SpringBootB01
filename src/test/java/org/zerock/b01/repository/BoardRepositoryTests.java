package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

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

    @Test
    public void testPaging(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending()); //Pageable , PageRequest.of - 페이징 처리

        Page<Board> result = boardRepository.findAll(pageable); //bno컬럼으로 내림차순 정렬하여 10개 데이터인 1번째 페이지를 조회
        //findAll리턴 타입으로 나오는 Page<T>타입은 페이징 처리에 필요한 여러 정보들 포함(다음,이전페이지 존재여부, 전체데이터 개수 등)

        log.info("total count : " + result.getTotalElements());
        log.info("total pages : " + result.getTotalPages());
        log.info("page number : " + result.getNumber());
        log.info("page size : " + result.getSize());

        log.info("---------------------------------");
        List<Board> todoList = result.getContent(); //페이징 처리 객체의 내용을 담음

        todoList.forEach(board -> log.info(board));
    }

    @Test
    public void testSearch1(){

        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll(){

        String[] types = {"t","c","w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info("전체페이지 개수 : " + result.getTotalPages());

        log.info("페이지 내 데이터 개수 : " + result.getSize());

        log.info("현재페이지번호 : " + result.getNumber());

        log.info("이전페이지 여부  : " + result.hasPrevious() + " 다음페이지 여부 : " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testSearchReplyCount(){
        String[] types = {"t", "c", "w"};

        String keyword = "24";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info("hasPrevious : " + result.hasPrevious() + " ,hasNext : " + result.hasNext());

        result.getContent().forEach(board -> {
            log.info(board);
        });
    }

    @Test
    public void testInsertWithImages(){

        Board board = Board.builder()
                .title("BoardImage_test")
                .content("BoardImageContent_test")
                .writer("tester")
                .build();

        for (int i = 0; i < 3; i++){
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg"); //uuid, fileName
        } //첨부파일 3개 등록
        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit //트랜잭션을 통과하면 commit처리
    public void testReadWithImage(){

//        Optional<Board> result = boardRepository.findById(1L);
        Optional<Board> result = boardRepository.findIdByWithImages(1L);

        Board board = result.orElseThrow(); //예외처리

        board.clearImages(); //BoardImage객체들 삭제 - 첨부파일을 추가할때 기존의 것은 삭제하고 추가해야하므로..

        log.info(board);
        log.info("----------------");
//        log.info(board.getImageSet()); //게시물에 등록된 이미지들 조회
        //Transactional 처리를 안하면 DB에 여러번 접근해야 하므로 No Session 에러 발생

//        for (BoardImage boardImage : board.getImageSet()){ //board객체에서 ImageSet객체가 있는 동안 반복
//            log.info(boardImage); //boardImage객체 로그생성
//        }
        
        for (int i = 0; i < 2; i++){ //BoardImage 객체 2개 생성
            board.addImage(UUID.randomUUID().toString(), "uploadfile + " + i + ".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit
    public void testRemovAll(){

        Long bno = 1L;

        replyRepository.deleteById(bno);

        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsertAll(){
        for (int i = 1; i <= 100; i++){

            Board board = Board.builder()
                    .title("Title.." + i)
                    .content("Content.." + i)
                    .writer("Wrtier.." + i)
                    .build();

            for (int j = 1; j < 3; j++){

                if (i % 5 == 0){
                    continue; //i가 5의 배수이면 내부for문 종료 - save실행
                }
                //i가 5의 배수가 아니면
                board.addImage(UUID.randomUUID().toString(), i + "file" + j + ".jpg");
            }
            boardRepository.save(board);
        }
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        boardRepository.searchWithALl(null, null, pageable);
    }

}
