package org.zerock.apiserver.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

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

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private LocalDate dueDate;


    public void TodoUpdate(String title, String content, boolean complete, LocalDate dueDate) {
        this.title = title;
        this.content = content;
        this.complete = complete;
        this.dueDate = LocalDate.from((TemporalAccessor) dueDate);
    }
}
