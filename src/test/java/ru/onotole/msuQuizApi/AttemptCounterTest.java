package ru.onotole.msuQuizApi;

import org.junit.Before;
import org.junit.Test;
import ru.onotole.msuQuizApi.model.AttemptCounter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

/**
 * Created by onotole on 18/04/2017.
 */
public class AttemptCounterTest {
    AttemptCounter counter = new AttemptCounter();
    @Before
    public void prepare() {
        counter.reset();
    }

    @Test
    public void checkCounter1() {
        assumeThat(counter.getAttemptCounter(), equalTo(3));
        assertTrue(counter.isAttemptLeft());
        assertTrue(counter.isAttemptLeft());
        assertTrue(counter.isAttemptLeft());
        assumeThat(counter.getAttemptCounter(), equalTo(0));
        assertFalse(counter.isAttemptLeft());
        counter.reset();
        assertTrue(counter.isAttemptLeft());
        assumeThat(counter.getAttemptCounter(), equalTo(2));
    }


}
