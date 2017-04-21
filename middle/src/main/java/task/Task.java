package task;

import lombok.Getter;
import javax.persistence.Entity;

/**
 * Created by onotole on 16/04/2017.
 */
@Getter
@Entity
public class Task {
    private final String Description;
    private final String answer;
}
