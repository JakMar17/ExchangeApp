package si.fri.jakmar.exchangeapp.backend.testingutility;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestingUtilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestingUtilityApplication.class, args);
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
