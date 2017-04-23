import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
