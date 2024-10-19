package org.zerock.apiserver.repository.search;


import com.querydsl.jpa.JPQLQuery;
import groovy.util.logging.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.apiserver.domain.QTodo;
import org.zerock.apiserver.domain.Todo;
import org.zerock.apiserver.dto.PageRequestDTO;

import java.util.List;

//query dsl을 사용할떄 인터페이스의 구현체는 이름 맞추고 +Impl로 끝나야함
@Log4j2
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {
    private static final Logger log = LoggerFactory.getLogger(TodoSearchImpl.class);

    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1(PageRequestDTO pageRequestDTO) {
        log.info("search1.................");
        QTodo todo = QTodo.todo;

        JPQLQuery<Todo>query = from(todo);


        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()-1,
                      pageRequestDTO.getSize(),
                      Sort.by("tno").descending());
        this.getQuerydsl().applyPagination(pageable, query);

        List<Todo> list = query.fetch();//목록 데이터 가져올떄
        long total = query.fetchCount();// 전부다 long타입으로나옴


        return new PageImpl<>(list, pageable,total);
    }
}
