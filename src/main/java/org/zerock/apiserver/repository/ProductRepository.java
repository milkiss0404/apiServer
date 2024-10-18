package org.zerock.apiserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.apiserver.domain.Product;
import org.zerock.apiserver.repository.search.ProductSearch;

import java.util.Objects;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {

    @EntityGraph(attributePaths = "imageList")//같이 묶을거
    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> selectOne(@Param("pno") Long pno);

    @Modifying//update delete 에서 쓰임
    @Query("update Product p set p.delFlag =:delFlag where p.pno =:pno")
    void updateToDelete(@Param("delFlag")boolean flag, @Param("pno")Long pno);


    @Query("select p,pi from Product p left join p.imageList pi where pi.ord = 0 and p.delFlag = false")
    //pageable 을 쓰면 항상 리턴타입 Page
    Page<Object[]> selectList(Pageable pageable);




}
