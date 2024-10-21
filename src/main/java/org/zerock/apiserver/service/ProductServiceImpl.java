package org.zerock.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.domain.ProductImage;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;
import org.zerock.apiserver.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;


    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable); //리스트안에 여러객체가 있다고보면됨
        //Object[] => 0 product 1 productImage 이렇게 배열로 가져옴


       List<ProductDTO>dtoList =  result.get().map(arr ->{
            ProductDTO productDTO = null;

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

           productDTO = ProductDTO.builder()
                   .pno(product.getPno())
                   .pname(product.getPname())
                   .pdesc(product.getPdesc())
                   .price(product.getPrice())
                   .build();

           String imageStr = productImage.getFileName();
           productDTO.setUploadedFileNames(List.of(imageStr));

           return productDTO;
       }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .total(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Long register(ProductDTO productDTO) {
        Product product = DtoTOEntity(productDTO);

        log.info("------------------------------");
        log.info(product);
        log.info(product.getImageList());
        Long pno = productRepository.save(product).getPno();
        return pno;
    }

    @Override
    public ProductDTO get(Long pno) {
        //jpa repository 는 반환타입이 optional 임 optional
        //orElseThrow()는
        // 기본적으로 비어 있는 Optional 객체가 있을 때 예외를 발생시키는 용도로 사용됩니다.
        //근데 exception 을 람다로 지정안해줘도 무관함
        Optional<Product> result = productRepository.findById(pno);
        Product product = result.orElseThrow();

        return EntityToDto(product);
    }

    @Override
    public void modify(ProductDTO productDTO) {
        //조회
        Optional<Product> result = productRepository.findById(productDTO.getPno());
        Product product = result.orElseThrow();
        //변경 내용 반영

        product.changePrice(productDTO.getPrice());
        product.changePname(productDTO.getPname());
        product.changePdesc(product.getPdesc());
        product.changeDel(productDTO.isDelFlag());

        //arrayList 를 바꾸면안댐..

        //이미지처리
        List<String> uploadedFileNames = productDTO.getUploadedFileNames();
        product.clearList();// 이거 객체안에 엘리먼트컬렉션 리스트 비워주는거임 헷갈리지마셈

        if (uploadedFileNames != null&& !uploadedFileNames.isEmpty()) {
          uploadedFileNames.forEach(uploadName ->{
              product.addImageString(uploadName);
            });
        }

        //저장
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        productRepository.deleteById(pno);

    }
}
