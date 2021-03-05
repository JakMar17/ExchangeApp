package si.fri.jakmar.exchangeapp.backend.testingutility.functions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestEnvironment {

    @Value("${environment.base.path}")
    private String basePath;
    @Value("${att.file.path}")
    private String attPath;

    public boolean create(String testId) {

        return false;
    }

    public boolean clean(String testId) {
        return false;
    }
}
