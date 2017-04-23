package ru.onotole.msuQuizApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by onotole on 16/04/2017.
 */
@Entity
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class Person {
    public static final Integer POST_LAST_TASK_ANSWER_FLAG = -1;
    public static final Integer INITIAL_TASK_ANSWER_FLAG = -2;
    public static final String WAITING_COMMAND_NAME_ANSWER_FLAG = "WAITING FOR COMMAND NAME 42";
    public static final String EMPTY_COMMAND_NAME_ANSWER_FLAG = "EMPTY COMMAND NAME 42";
    public static final Integer DEFAULT_ATTEMPTS = 3; //Integer.valueOf(System.getProperty("default_attempts_count"));

    @Id
    private Long id;
    private String commandName = EMPTY_COMMAND_NAME_ANSWER_FLAG;
    private String taskOrder; // 3,1,0,2
    private Integer expectedAnswer = INITIAL_TASK_ANSWER_FLAG;
    private Integer balls = 0;
    private LocalDateTime start = LocalDateTime.now();
    private LocalDateTime finish;
    private Integer attemptCounter = DEFAULT_ATTEMPTS;
    public void addWinPoint() {
        balls++;
    }

    public Integer getNextTaskNumber() {
        Integer nextTaskNumber;
        if (taskOrder.contains(",")) {
            nextTaskNumber = Integer.valueOf(taskOrder.split(",")[0]);
            taskOrder = taskOrder.replaceFirst("\\d+,","");
        } else if (taskOrder.length() > 0) {
            nextTaskNumber = Integer.valueOf(taskOrder);
            taskOrder = "";
        } else {
            nextTaskNumber = POST_LAST_TASK_ANSWER_FLAG;
        }

        resetAttemptsCounter();
        return nextTaskNumber;
    }

    public void resetAttemptsCounter() {
        attemptCounter = DEFAULT_ATTEMPTS;
    }

    public boolean makeAttempt() {
        attemptCounter--;
        if (attemptCounter < 1) {
            return false;
        } else {
           return true;
        }
    }


    public Integer getAttemptCounter() {
        return attemptCounter;
    }

    public String finish() {
        if (finish == null) {
            finish = LocalDateTime.now();
        }
        log.error("XXXX" + start);
        log.error("XXXX" + finish);
        Duration delta = Duration.between(start, finish);
        long hours = delta.toHours();
        long minutes = delta.toMinutes() % 60;
        long seconds = delta.getSeconds() % 60;
        return String.format(Phrases.CONGRATULATION, getCommandName(), hours, minutes, seconds, balls);
    }

}
