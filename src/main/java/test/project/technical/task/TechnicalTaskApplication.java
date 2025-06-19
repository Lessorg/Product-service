package test.project.technical_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TechnicalTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechnicalTaskApplication.class, args);
    }

}
