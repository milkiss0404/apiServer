package org.zerock.apiserver.service;

import jakarta.transaction.Transactional;
import org.zerock.apiserver.domain.Todo;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.TodoDTO;

import java.time.LocalDate;

@Transactional
public interface TodoService {

    TodoDTO get(Long tno);

    Long register(TodoDTO dto);

    PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO);

    void modify(TodoDTO dto);

    void remove(Long tno);

    default TodoDTO entityToDTO(Todo todo) {
        return  TodoDTO.builder()
                .tno(todo.getTno())
                .title(todo.getTitle())
                .content(todo.getContent())
                .complete(todo.isComplete())
                .dueDate(todo.getDueDate() != null ? LocalDate.from(todo.getDueDate()) : null)  // Null 체크
                .build();
    }

    default Todo dtoToEntity(TodoDTO todoDTO) {

        return  Todo.builder()
                .tno(todoDTO.getTno())
                .title(todoDTO.getTitle())
                .content(todoDTO.getContent())
                .complete(todoDTO.isComplete())
                .dueDate(LocalDate.from(todoDTO.getDueDate().atStartOfDay()))
                .build();
    }
}
