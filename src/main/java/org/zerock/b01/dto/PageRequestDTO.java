package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
    
    private String type; //검색유형
    
    private String keyword; //검색어

    private String link;

    public String[] getTypes(){
        if (type == null || type.isEmpty()){ //검색유형 없다면 null 반환
            return null;
        }
        return type.split(""); //검색유형 있다면 잘라서 배열로 반환
    }

    public Pageable getPageable(String...props){
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    public String getLink(){
        if (link == null){ //link가 없으면
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

            builder.append("&size=" + this.size);

            if (type != null && type.length() > 0){
                builder.append("&type=" + type);
            }

            if (keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                }catch (UnsupportedEncodingException e){

                }
            }
            link = builder.toString();
        }
        return link;
    } //getLink
}
