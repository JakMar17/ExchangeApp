package si.fri.jakmar.backend.exchangeapp.containers;

import java.util.Optional;

public class SuccessErrorContainer<S, E> {
    private final S success;
    private final E error;

    public SuccessErrorContainer(S success, E error) {
        this.success = success;
        this.error = error;
    }

    public Optional<S> getSuccess() {
        return Optional.of(success);
    }

    public Optional<E> getError() {
        return Optional.of(error);
    }
}
