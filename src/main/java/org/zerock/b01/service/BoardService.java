package org.zerock.b01.service;

import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno); //단건조회

    void modify(BoardDTO boardDTO); //수정
    
    void remove(Long bno); //삭제

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
}
