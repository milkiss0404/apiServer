package org.zerock.apiserver.repository.search;


import com.querydsl.jpa.JPQLQuery;
import groovy.util.logging.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.apiserver.domain.QTodo;
import org.zerock.apiserver.domain.Todo;

//query dsl을 사용할떄 인터페이스의 구현체는 이름 맞추고 +Impl로 끝나야함
@Log4j2
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {
    private static final Logger log = LoggerFactory.getLogger(TodoSearchImpl.class);

    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1() {
        log.info("search1.................");
        QTodo todo = QTodo.todo;
        JPQLQuery<Todo>query = from(todo);
        query.where(todo.title.contains("1"));

        Pageable pageable = PageRequest.of(1, 10, Sort.by("tno").descending());
        this.getQuerydsl().applyPagination(pageable, query);

        query.fetch(); //목록 데이터 가져올떄
        query.fetchCount(); // 전부다 long타입으로나옴

        return null;
    }
}
