package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.service.BoardService;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @RequestMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO); //BoardDTO타입으로 페이징된 데이터가 리스트화되어 responseDTO에 저장
        //PageResponseDTO타입으로..

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO); //PageResponseDTO와 PageRequestDTO객체가 화면에 전달됨(model을 통해서)
        //responseDTO라는 이름으로 위에서 정의한 responseDTO객체가 model에 저장되어 view에서 사용할 수 있음
    } //list
}
