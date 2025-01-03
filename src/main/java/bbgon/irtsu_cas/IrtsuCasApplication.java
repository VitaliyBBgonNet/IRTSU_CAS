package bbgon.irtsu_cas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IrtsuCasApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrtsuCasApplication.class, args);
    }

}
