package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;
import org.zerock.apiserver.service.ProductService;
import org.zerock.apiserver.util.CustomFileUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    private final ProductService productService;

    @PostMapping("/")
    public Map<String,Long>register(ProductDTO productDTO) throws InterruptedException {
        log.info("register : "+productDTO );

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadedFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadedFileNames(uploadedFileNames);

        log.info(uploadedFileNames);

        Long pno = productService.register(productDTO);

        Thread.sleep(2000);

        return Map.of("result", pno);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName")String fileName) {
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        return productService.getList(pageRequestDTO);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno")Long pno) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> update(@PathVariable("pno")Long pno, ProductDTO productDTO) {

        productDTO.setPno(pno);
        //기존 상품 조회
        ProductDTO oldProductDTO = productService.get(pno);

        //file upload
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        //keep files
        List<String> uploadedFileNames = productDTO.getUploadedFileNames();

        //새로 업로드된파일이 있다면
        if(currentUploadFileNames !=null && !currentUploadFileNames.isEmpty()){
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDTO);

        List<String> oldFileNames = oldProductDTO.getUploadedFileNames();

        if (oldFileNames != null && !oldFileNames.isEmpty()) {
            List<String> removeFileName = oldFileNames.stream().filter(fileName ->
                    uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

            fileUtil.deleteFiles(removeFileName);
        }
        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("{pno}")
    public Map<String ,String> remove(@PathVariable("pno")Long pno) {

        List<String> oldFileNames = productService.get(pno).getUploadedFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");
    }

}
