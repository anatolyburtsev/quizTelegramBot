package ru.onotole.msuQuizApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

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
    private LocalDateTime start = LocalDateTime.now();
    private LocalDateTime finish;
    private AttemptCounter attemptCounter = new AttemptCounter();
    public void addWinPoint() {
        balls++;
    }

    public Integer getNextTaskNumber() {
        Integer nextTaskNumber;
        switch (taskOrder.length()) {
            case 0:
                nextTaskNumber = -1;
                break;
            case 1:
                nextTaskNumber = Integer.valueOf(taskOrder);
                taskOrder = "";
                break;
            default:
                nextTaskNumber = Integer.valueOf(taskOrder.split(",")[0]);
                taskOrder = taskOrder.replaceFirst("\\d+,","");
        }
        attemptCounter.reset();
        return nextTaskNumber;
    }

    public String finish() {
        if (finish == null) {
            finish = LocalDateTime.now();
        }
        Duration delta = Duration.between(start, finish);
        long hours = delta.toHours();
        long minutes = delta.toMinutes() % 60;
        long seconds = delta.getSeconds() % 60;
        return String.format("Поздравляю! Ты прошел игру за %d:%d:%d и набрал %d баллов!", hours, minutes, seconds, balls);
    }

}
