package bf.multi.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BfUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(BfUserApplication.class, args);
    }
}
