package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.b01.dto.upload.UploadFileDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController //Controller와 차이점은 반환값에 자동으로 @ResponseBody 어노테이션이 적용된다는 것 -> HTTP응답 본문(Response Body)에 자바객체가 매핑되어 클라에 전달됨
@Log4j2
public class UpDownController {

    @Value("${org.zerock.upload.path}") //application.properties에 지정된 경로로 설정 org.zerock.upload.path (C:\\upload)
    private String uploadPath;

    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(UploadFileDTO uploadFileDTO){

        log.info(uploadFileDTO);

        if (uploadFileDTO.getFiles() != null){

            uploadFileDTO.getFiles().forEach(multipartFile -> {
//                log.info(multipartFile.getOriginalFilename()); //파일의 원본명
                String originalName = multipartFile.getOriginalFilename(); //파일원본명 originalName 변수에 할당
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

                try {
                    multipartFile.transferTo(savePath); //transferTo : 지정 경로에 파일을 저장(여기서는 savePath 경로에 저장)
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
        }

        return null;
    }

}
