package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.service.ReplyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController //메서드의 모든 리턴값은 jsp나 thymeleaf가(기존의 @Controller) 아닌 바로 json이나 xml로 처리됨
@RequestMapping("/replies")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {

    private final ReplyService replyService;

    @ApiOperation(value = "Replies POST", notes = "POST방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    //consumes : 해당 메서드를 받아 소비(consume)하는 데이터가 어떤 종류인지 명시 가능. 여기서는 json타입의 데이터를 처리하는 메서드임을 명시
//    public ResponseEntity<Map<String, Long>> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException {
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException {
        //@RequestBody : json문자열을 replyDTO로 변환하기 위해 사용

        log.info(replyDTO);

        if (bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO); //service의 reigster로 등록

        resultMap.put("rno", rno);

//        return ResponseEntity.ok(resultMap);
        return resultMap;
    } //메서드 리턴값에 문제가 있다면 BindException과 bindngResult를 통해 @RestControllerAdvice(CustomRestAdvice class)가 처리할 것이므로 정상적인 결과만 리턴

    @ApiOperation(value = "Replies of board", notes = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping("/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO){
        //@PathVariable : 호출하는 경로의 값을 직접 파라미터 변수로 처리

        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    } //특정 게시물의 댓글 목록 : replies/list/{bno}

    @ApiOperation(value = "Read Reply", notes = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno){

        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }

    @ApiOperation(value = "Delete Reply", notes = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno){

        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE) //json문자열 전송처리
    public Map<String, Long> remove(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){ //RequestBody를 통해 본문(body)에 replyDTO 처리

        replyDTO.setRno(rno);

        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }


}
