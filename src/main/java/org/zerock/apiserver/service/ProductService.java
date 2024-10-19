package org.zerock.apiserver.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;

import java.util.List;

@Transactional
public interface ProductService {

    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    default Product DtoTOEntity(ProductDTO productDTO){
        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();
        List<String> uploadFileNames = productDTO.getUploadedFileNames();

        //엔티티안에있는컬렉션은 새로만들면안됀다

        if (uploadFileNames == null || uploadFileNames.isEmpty()) {
            return product;
        }

        for (String filename : uploadFileNames) {
            product.addImageString(filename);
        }
        return product;
    }

    default ProductDTO EntityToDto(Product product){
        return ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();
    }

    Long register(ProductDTO productDTO);

}
