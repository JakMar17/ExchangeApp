package si.fri.jakmar.exchangeapp.backend.testingutility.models.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestRequestBody {
    private Integer assignmentId;
    private Integer[] submissionIds;
}
