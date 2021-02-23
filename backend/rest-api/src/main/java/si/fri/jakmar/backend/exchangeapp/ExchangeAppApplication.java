package si.fri.jakmar.backend.exchangeapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import si.fri.jakmar.backend.exchangeapp.storage.FileStorageService;

import javax.annotation.Resource;
import java.util.Scanner;

@SpringBootApplication
public class ExchangeAppApplication implements CommandLineRunner {

    @Resource
    private FileStorageService fileStorageService;

    public static void main(String[] args) {
        SpringApplication.run(ExchangeAppApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        fileStorageService.init();
    }
}
