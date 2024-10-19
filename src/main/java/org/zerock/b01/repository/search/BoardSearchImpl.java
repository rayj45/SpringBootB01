package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl(){
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        QBoard board = QBoard.board; //Q객체 생성

        JPQLQuery<Board> query = from(board); //select * from board;

        query.where(board.title.contains("1")); //where title like "%" 1 "%"

        this.getQuerydsl().applyPagination(pageable, query); //querydsl에서의 페이징 처리

        List<Board> list = query.fetch(); //fetch : JPQL의 실행

        long count = query.fetchCount(); //fetchCount : JPQL의 count쿼리 실행

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if ( (types != null && types.length > 0) && keyword != null ){ //검색유형과 검색어가 있다면
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for (String type : types){

                switch (type){
                    case "t" : //검색유형이 t이면
                        booleanBuilder.or(board.title.contains(keyword)); //제목(title)을 기준으로 검색어를 포함한 내용을 검색
                        break;

                    case "c" : //검색유형이 c이면
                        booleanBuilder.or(board.content.contains(keyword)); //내용(content)를 기준으로 검색어를 포함한 내용을 검색
                        break;

                    case "w" : //검색유형이 w이면
                        booleanBuilder.or(board.writer.contains(keyword)); //글쓴이(writer)를 기준으로 검색어를 포함한 내용을 검색
                        break;
                } //switch
            } //for
            query.where(booleanBuilder); //select * from board where booleanBuilder : 검색조건에 booleanBuilder에서 설정한 내용이 걸리는 것
        } //if
        query.where(board.bno.gt(0L)); //where board.bno > 0 조건설정

        this.getQuerydsl().applyPagination(pageable, query); //페이징

        List<Board> list = query.fetch(); //쿼리 실행

        long count = query.fetchCount(); //count쿼리 담음

        return new PageImpl<>(list, pageable, count); //Page<T>타입을 반환 : list - 실제목록데이터 , pageable : 페이징객체, long : 데이터전체개수
    } //searchAll

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board); //select * from board
        query.leftJoin(reply).on(reply.board.eq(board)); //left join on reply.board.bno = board.bno

        query.groupBy(board); //group by

        if ( (types != null && types.length > 0) && keyword != null ){ //검색유형과 검색어가 있다면

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for (String type : types){

                switch (type){
                    case "t" : //검색유형이 t이면
                        booleanBuilder.or(board.title.contains(keyword)); //제목(title)을 기준으로 검색어를 포함한 내용을 검색
                        break;

                    case "c" : //검색유형이 c이면
                        booleanBuilder.or(board.content.contains(keyword)); //내용(content)를 기준으로 검색어를 포함한 내용을 검색
                        break;

                    case "w" : //검색유형이 w이면
                        booleanBuilder.or(board.writer.contains(keyword)); //글쓴이(writer)를 기준으로 검색어를 포함한 내용을 검색
                        break;
                } //switch
            } //for
            query.where(booleanBuilder); //select * from board where booleanBuilder : 검색조건에 booleanBuilder에서 설정한 내용이 걸리는 것
        } //if

        query.where(board.bno.gt(0L)); //bno > 0

        //projection : JPQL의 결과를 바로 DTO로 처리하는 기능
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.
                bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                ));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithALl(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board); //select * from board;
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); //reply의 board객체랑 board객체와 같은 것을 on조건절로 leftJoin

        getQuerydsl().applyPagination(pageable, boardJPQLQuery); //paging

        List<Board> boardList = boardJPQLQuery.fetch(); //조인한 쿼리를 fetch

        boardList.forEach(board1 -> {
            System.out.println(board1.getBno());
            System.out.println(board1.getImageSet());
            System.out.println("------------------");
        });

        return null;
    }
}
