package ru.onotole.msuQuizApi.model;

import lombok.Getter;

/**
 * Created by onotole on 18/04/2017.
 */
@Getter
public class AttemptCounter {
    private Integer attemptCounter;
    private static final Integer DEFAULT_ATTEMPT_COUNTER = 3;

    public AttemptCounter() {
        reset();
    }

    public void reset() {
        attemptCounter = DEFAULT_ATTEMPT_COUNTER;
    }

    public Integer get() {
        return attemptCounter;
    }

    public boolean isAttemptLeft() {
        if (attemptCounter < 1) {
            return false;
        } else {
           attemptCounter--;
           return true;
        }
    }

}
