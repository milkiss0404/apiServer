package org.zerock.apiserver.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    private boolean delFlag;


    //처리용
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    //조회용
    @Builder.Default
    private List<String> uploadedFileNames = new ArrayList<>();
}
