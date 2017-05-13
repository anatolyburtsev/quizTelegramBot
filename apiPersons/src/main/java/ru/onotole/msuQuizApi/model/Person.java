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
import java.util.Deque;
import java.util.LinkedList;

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
    public static final Integer DEFAULT_ATTEMPTS = 3;
    public static final Integer TIME_FOR_GAME = 45;

    @Id
    private Long id;
    private String commandName = EMPTY_COMMAND_NAME_ANSWER_FLAG;
    private LinkedList<Integer> taskOrder;
    private Integer expectedAnswer = INITIAL_TASK_ANSWER_FLAG;
    private Integer balls = 0;
    private LocalDateTime start;// = LocalDateTime.now();
    private LocalDateTime finish;
    private Integer attemptCounter = DEFAULT_ATTEMPTS;
    public void addWinPoint() {
        balls++;
    }

    public Integer getNextTaskNumber() {
        Integer nextTaskNumber;
        if (taskOrder.isEmpty()) {
            nextTaskNumber = POST_LAST_TASK_ANSWER_FLAG;
        } else {
            nextTaskNumber = taskOrder.pop();
        }
        resetAttemptsCounter();
        return nextTaskNumber;
    }

    public void resetAttemptsCounter() {
        attemptCounter = DEFAULT_ATTEMPTS;
    }

    public boolean makeAttempt() {
        return --attemptCounter >= 1;
    }


    public Integer getAttemptCounter() {
        return attemptCounter;
    }

    public String finish() {
        if (finish == null) {
            finish = LocalDateTime.now();
        }
        Duration delta = Duration.between(start, finish);
        long hours = delta.toHours();
        long minutes = delta.toMinutes() % 60;
        long seconds = delta.getSeconds() % 60;
        return String.format(Phrases.CONGRATULATION, getCommandName(), hours, minutes, seconds, balls);
    }

}
