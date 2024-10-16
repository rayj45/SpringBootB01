package org.zerock.b01.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {

    private String uuid;

    private String fileName;

    private boolean img; //이미지여부

    public String getLink(){ //json으로 처리될 때 link속성으로 자동 처리
        if (img){ //이미지인 경우
            return "s_" + uuid + "_" + fileName; //썸네일 반환
        }else {
            return uuid + "_" + fileName; //uuid만 앞에 붙여반환
        }
    }
}
