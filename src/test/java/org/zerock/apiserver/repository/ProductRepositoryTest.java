package org.zerock.apiserver.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.dto.PageRequestDTO;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert() {
        for (int i = 0; i <10; i++) {


            Product product = Product.builder().pname("test").pdesc("test DESC").price(100000).build();

            product.addImageString(UUID.randomUUID() + "_" + "image1.jpg");
            product.addImageString(UUID.randomUUID() + "_" + "image2.jpg");

            productRepository.save(product);
        }
    }

    @Transactional
    @Test
    @Commit
    public void testDelete() {
        Long pno=2L;

        productRepository.updateToDelete(true,pno);
    }


    @Test
    public void testUpdate() {
        Product product  = productRepository.selectOne(1L).get(); //Optional 타입은 get 을써야 가져올수있음
        product.changePrice(30000000);

        product.clearList();
        product.addImageString(UUID.randomUUID()+"_"+"Pimage1.jpg");
        product.addImageString(UUID.randomUUID()+"_"+"Pimage2.jpg");
        product.addImageString(UUID.randomUUID()+"_"+"Pimage3.jpg");

        productRepository.save(product);
    }

    @Test
    public void TestList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());
        Page<Object[]> result = productRepository.selectList(pageable);
            //Page 는  Slice 를 상속 받고 있음
        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));

    }


    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        productRepository.searchList(pageRequestDTO);


    }
}