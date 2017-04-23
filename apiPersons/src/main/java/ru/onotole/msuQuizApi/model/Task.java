package ru.onotole.msuQuizApi.model;

import lombok.*;
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
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String description;
    private Integer answer;

}
