package ru.onotole.msuQuizApi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by onotole on 16/04/2017.
 */
@Entity
@ToString
//@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Getter
//@Accessors(chain = true)
public class Task {

    public Task(String description, Integer answer) {
        this.description = description;
        this.answer = answer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String description;
    private Integer answer;
}
