package org.zerock.b01.service;

import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;

public interface ReplyService {
    Long register(ReplyDTO replyDTO); //등록
    
    ReplyDTO read(Long rno); //상세조회
    
    void modify(ReplyDTO replyDTO); //수정
    
    void delete(Long rno); //삭제

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO); //댓글 페이징구현
}
