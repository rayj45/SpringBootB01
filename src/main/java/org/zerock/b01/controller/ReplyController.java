package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.b01.dto.ReplyDTO;

import java.util.HashMap;
import java.util.Map;

@RestController //메서드의 모든 리턴값은 jsp나 thymeleaf가(기존의 @Controller) 아닌 바로 json이나 xml로 처리됨
@RequestMapping("/replies")
@Log4j2
public class ReplyController {

    @ApiOperation(value = "Replies POST", notes = "POST방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    //consumes : 해당 메서드를 받아 소비(consume)하는 데이터가 어떤 종류인지 명시 가능. 여기서는 json타입의 데이터를 처리하는 메서드임을 명시
    public ResponseEntity<Map<String, Long>> register(@RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException {
        //@RequestBody : json문자열을 replyDTO로 변환하기 위해 사용

        log.info(replyDTO);

        if (bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", 510L);

        return ResponseEntity.ok(resultMap);
    } //메서드 리턴값에 문제가 있다면 BindException과 bindngResult를 통해 @RestControllerAdvice(CustomRestAdvice class)가 처리할 것이므로 정상적인 결과만 리턴

}
