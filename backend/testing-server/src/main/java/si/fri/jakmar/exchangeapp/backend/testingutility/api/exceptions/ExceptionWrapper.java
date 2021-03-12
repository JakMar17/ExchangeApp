package si.fri.jakmar.exchangeapp.backend.testingutility.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionWrapper<T> {
    private T body;
}
