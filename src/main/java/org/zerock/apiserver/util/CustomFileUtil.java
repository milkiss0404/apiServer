package org.zerock.apiserver.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct //업로드 폴더 만들어주기
    public void init() {
        File tempFolder = new File(uploadPath);
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }
    }

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        if (files == null || files.isEmpty()) {
            return null;
        }// 파일이 null이나 비어있으면 리턴


        List<String> uploadNames = new ArrayList<>();
        String savedName = null;
        for (MultipartFile file : files) {
            savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, savedName); //(위치, 이름) 위치에다가 이 이름으로 만들거임

            try {
                Files.copy(file.getInputStream(),savePath); //원본파일 업로드

                String contentType = file.getContentType(); //Mine Type


                //이미지 파일이라면
                if (contentType != null||contentType.startsWith("image")) {
                    Path thumbNailPath = Paths.get("s_" + savedName);
                    Thumbnails.of(savePath.toFile()).size(200,200).toFile(thumbNailPath.toFile()); //썸네일 저장
                }


                uploadNames.add(savedName);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }//end for

        return uploadNames;
    }





    public ResponseEntity<Resource>getFile(String fileName) {    //uploadPath폴더내에 이미지 파일 읽어와서 ResponseEntity 로 전송
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName); //File.separator ==/ 임 슬래시임 슬래시
       if(!resource.isReadable()){
           resource = new FileSystemResource(uploadPath+File.separator+"fuck.jpg");
       }
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public void deleteFiles(List<String> fileNames) {
        if (fileNames == null||fileNames.isEmpty()) {
            return;
        }
        fileNames.forEach(fileName->{
            //썸네일 삭제
            String thumbnailFileName = "s_" + fileName;

            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }
}
