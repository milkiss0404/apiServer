package org.zerock.apiserver.domain;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.apiserver.repository.TodoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@SpringBootTest
@Log4j2
class TodoTest {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test1(){
        Assertions.assertNotNull(todoRepository);
        log.info(todoRepository.getClass().getName());
    }

    @Test
    public void testInsert() {
       Todo todo =  Todo.builder()
                .title("title")
                .content("content")
                .dueDate(LocalDate.of(2023,12,30).atStartOfDay())
                .build();
        todoRepository.save(todo);
    }
    @Test
    public void testRead() {

        Long tno = 1L;
        Optional<Todo> byId = todoRepository.findById(tno);
        Todo todo = byId.orElseThrow();
        log.info(todo);
    }

    @Test
    public void testUpdate(){
        //given
        Long tno = 1L;

        Todo existingTodo = todoRepository.findById(tno)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with tno: " + tno));


        Todo update = Todo.builder()
                .tno(existingTodo.getTno())
                .title("타이틀")
                .content("dasdasdas")
                .build();

        //when
        Todo updatedTodo = todoRepository.save(update);

        //then
        log.info("Updated Todo: {}", updatedTodo);
    }

    @Test
    public void testPaging() {
        for (int i = 0; i < 100; i++) {
            Todo update = Todo.builder()
                    .title("타이틀")
                    .content("dasdasdas")
                    .build();
            Todo save = todoRepository.save(update);

        }
        //페이지번호 0부터
        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());
        Page<Todo> result = todoRepository.findAll(pageable);

        log.info(result.getTotalElements());
        log.info(result.getContent());

    }

//    @Test
//    public void testSearch1() {
//        todoRepository.search1();
//    }
}