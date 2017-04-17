package ru.onotole.msuQuizApi.model;

import com.sun.deploy.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

/**
 * Created by onotole on 16/04/2017.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class Person {
    @Id
    private Long id;
    // 3,1,4,2
    private String taskOrder;
    private Integer expectedAnswer;
    private Integer balls = 0;

    public void inc() {
        balls++;
    }

    public Integer getNextTaskNumber() {
        if (taskOrder.length() < 1) {
            return -1;
        }
        Integer nextTaskNumber = Integer.valueOf(taskOrder.split(",")[0]);
        taskOrder = taskOrder.replace("\\d+,","");
        return nextTaskNumber;
    }

    public String finish() {
        return "Поздравляю! Ты прошел игру и набрал " + balls + " баллов!";
    }

}
