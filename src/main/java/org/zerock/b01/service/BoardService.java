package org.zerock.b01.service;

import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

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

    default Board dtoToEntity(BoardDTO boardDTO){ //dto -> Entity 변환 *service에서 구현을 해서 Impl에서 override필요없음!
        Board board = Board.builder() //엔티티객체 생성하여 dto에서 받은 값들을 넣어줌
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if (boardDTO.getFileNames() != null) { //dto에 첨부파일이 있다?!
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_"); //_로 끊어서 변수 arr에 할당
                board.addImage(arr[0], arr[1]); //uuid, fileName
            });
        }
        return board;
    } //dtoToEntity

    default BoardDTO entityToDto(Board board){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        //fileName을 설정
        //FileNames는 List형태
        List<String> fileName = board.getImageSet().stream().sorted().map(boardImage ->
                boardImage.getUuid() + "_" + boardImage.getFileName())
                .collect(Collectors.toList());

        boardDTO.setFileNames(fileName);

        return boardDTO;
    }


}
