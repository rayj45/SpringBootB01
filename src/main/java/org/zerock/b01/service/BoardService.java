package org.zerock.b01.service;

import org.zerock.b01.dto.*;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno); //단건조회

    void modify(BoardDTO boardDTO); //수정
    
    void remove(Long bno); //삭제

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO); //BoardDTO 타입으로 페이징 리스트

    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
    //BoardListReplyCountDTO(댓글반영된 boardDTO) 타입으로 페이징 리스트

    //게시글 이미지와 댓글숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);
}
