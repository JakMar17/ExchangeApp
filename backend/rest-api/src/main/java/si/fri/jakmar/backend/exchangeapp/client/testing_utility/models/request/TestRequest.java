package si.fri.jakmar.backend.exchangeapp.client.testing_utility.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestRequest {
    private Integer assignmentId;
    private Integer[] submissionIds;
}
