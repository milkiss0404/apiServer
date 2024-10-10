package org.zerock.apiserver.service;

import lombok.extern.log4j.Log4j2;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class TodoServiceImplTest {

    @Autowired
    TodoService todoService;

    @Test
    public void testGet() {
        Long tno =50L;

        log.info(todoService.get(tno));
    }

}