package si.fri.jakmar.exchangeapp.backend.testingutility.containers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Pair<A, B> {
    public A first;
    public B second;

    @Override
    public String toString() {
        return first.toString() + ": " + second.toString();
    }
}
