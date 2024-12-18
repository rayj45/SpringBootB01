package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.BoardRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional //해당객체를 감싸는 별도의 클래스를 생성함. 여러번의 db연결을 방지하기 위해 기본으로 사용하는 것이 좋음
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;


    @Override
    public Long register(BoardDTO boardDTO) {

//        Board board = modelMapper.map(boardDTO, Board.class);
        Board board = dtoToEntity(boardDTO); //modelmapper대신 dtoToEntiy사용(메서드를 default접근제어자로 설정해놨기에 override없이 사용가능
        //addImage메서드 때문애 받은 boardDTO에 fileName이 있으면 boardImage객체에 fileName넣어줌

        Long bno = boardRepository.save(board).getBno(); //save메서드 : board객체를 db에 저장(register)하고, 저장한 board객체의 bno를 bno변수에 저장

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {

        //Optional<Board> result = boardRepository.findById(bno);
        Optional<Board> result = boardRepository.findIdByWithImages(bno); //image와 같이 조회

        Board board = result.orElseThrow(); //예외처리

//        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        BoardDTO boardDTO = entityToDto(board); //modelmapper대신 image까지 함께 반환하는 entityToDTO메서드 사용

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        //매개변수로 받은 DTO객체에서 bno를 받아 repository에서 객체를 찾아 result변수에 할당

        Board board = result.orElseThrow(); //예외처리

        board.change(boardDTO.getTitle(), boardDTO.getContent()); //change메서드로 제목,내용 변경

        board.clearImages(); //board객체에 등록된 이미지들 모두 제거

        if (boardDTO.getFileNames() != null){ //boardDTO의 파일명이 있으면
            for (String fileName : boardDTO.getFileNames()){
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }

        boardRepository.save(board); //수정된 board객체를 저장
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithALl(types, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }
}
