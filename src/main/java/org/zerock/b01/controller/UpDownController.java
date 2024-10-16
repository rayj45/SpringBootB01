package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.upload.UploadFileDTO;
import org.zerock.b01.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController //Controller와 차이점은 반환값에 자동으로 @ResponseBody 어노테이션이 적용된다는 것 -> HTTP응답 본문(Response Body)에 자바객체가 매핑되어 클라에 전달됨
@Log4j2
public class UpDownController {

    @Value("${org.zerock.upload.path}") //application.properties에 지정된 경로로 설정 org.zerock.upload.path (C:\\upload)
    private String uploadPath;

    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public String upload(UploadFileDTO uploadFileDTO){
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){

        log.info(uploadFileDTO);

        if (uploadFileDTO.getFiles() != null){

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {
//                log.info(multipartFile.getOriginalFilename()); //파일의 원본명
                String originalName = multipartFile.getOriginalFilename(); //파일원본명 originalName 변수에 할당

                log.info(originalName);

                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

                boolean image = false;

                try {
                    multipartFile.transferTo(savePath); //transferTo : 지정 경로에 파일을 저장(여기서는 savePath이름으로 저장)

                    if (Files.probeContentType(savePath).startsWith("image")){ //savePath파일 중에 image파일이라면

                        image = true;

                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName); //썸네일 파일명 새로 파서 thumbFile에 할당

                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                                .uuid(uuid)
                                .fileName(originalName)
                                .img(image)
                                .build());

            }); //forEach

            return list;
        } //if

        return null;
    } //upload

    @ApiOperation(value = "view 파일", notes = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        String resourceName = resource.getFilename(); //저장한 resource의 파일명
        HttpHeaders headers = new HttpHeaders(); //Http헤더객체 생성

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath())); //probeContentType - 마임타입(Content-Type)설정
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        } //try-catch

        return ResponseEntity.ok().headers(headers).body(resource);
    } // viewFileGet

    @ApiOperation(value = "remove 파일", notes = "DELETE 방식으로 첨부파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();

        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());

            removed = resource.getFile().delete(); //해당 파일이 삭제결과(0,1)을 removed 변수에 할당

            if (contentType.startsWith("image")){ //섬네일이 존재한다면
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName); //섬네일 파일을 찾아서

                thumbnailFile.delete(); //섬네일 파일도 삭제
            }

        }catch (Exception e){
            log.error(e.getMessage());
        } //try-catch

        resultMap.put("result", removed); //result에 삭제 결과를 담아 put

        return resultMap;
    }

} //class
