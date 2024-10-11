package org.zerock.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    @Column(length = 500,nullable = false)
    private String title;
    private String content;
    private boolean complete;
    private LocalDateTime dueDate;


    public void TodoUpdate(String title, String content, boolean complete, LocalDateTime dueDate) {
        this.title = title;
        this.content = content;
        this.complete = complete;
        this.dueDate = dueDate;
    }
}
