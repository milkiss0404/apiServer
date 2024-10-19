package org.zerock.apiserver.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {
    private List<E> dtoList; //다양한 타입의  DTO 를 보내줄수있음

    private List<Integer> pageNumList;

    private PageRequestDTO pageRequestDTO;

    private boolean prev, next;

    private int totalCount,prevPage,nextPage,totalPage,current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long total) { //jpa는 count  숫자가 long으로나온다??
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) total;

        //끝페이지
        //ceil == 올림
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;
        int start = end -9;

        //진짜 마지막 페이지 그니까 끝 페이지
        int last = (int)(Math.ceil(totalCount / (double) pageRequestDTO.getSize()));

        end = end >last ? last :end;

        this.prev = start > 1; //prev는 boolean
        this.next = totalCount > end * pageRequestDTO.getSize(); //size는 페이지당 몇개

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        //startInclusive: 범위의 시작값 (포함)
        //endInclusive: 범위의 끝값 (포함)
        //IntStream.rangeClosed(1, 5)
        //.forEach(System.out::println);
        this.prevPage = start > 1 ? start -1 :0;
        this.nextPage = next ? end + 1 : 0;
    }
}
