package ru.onotole.msuQuizApi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by onotole on 16/04/2017.
 */
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Getter
public class Task {

    public Task(String description, Integer answer) {
        this.description = description;
        this.answer = answer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Lob
    @Column(length = 20520)
    private String description;
    private Integer answer;
}
