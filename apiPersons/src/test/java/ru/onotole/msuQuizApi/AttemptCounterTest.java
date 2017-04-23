package ru.onotole.msuQuizApi;

import org.junit.Before;
import org.junit.Test;
import ru.onotole.msuQuizApi.model.Person;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

/**
 * Created by onotole on 18/04/2017.
 */
public class AttemptCounterTest {
    Person person = new Person();

    @Before
    public void prepare() {
        person.resetAttemptsCounter();
    }

    @Test
    public void checkCounter1() {
        assumeThat(person.getAttemptCounter(), equalTo(3));
        assertTrue(person.makeAttempt());
        assertTrue(person.makeAttempt());
        assertFalse(person.makeAttempt());
        assumeThat(person.getAttemptCounter(), equalTo(0));
        assertFalse(person.makeAttempt());
        person.resetAttemptsCounter();
        assertTrue(person.makeAttempt());
        assumeThat(person.getAttemptCounter(), equalTo(2));
    }


}
