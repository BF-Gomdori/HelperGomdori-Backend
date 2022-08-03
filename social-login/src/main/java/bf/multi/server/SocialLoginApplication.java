package bf.multi.server;

import bf.multi.server.config.properties.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        CorsProperties.class,
        AppProperties.class
})
public class SocialLoginApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialLoginApplication.class, args);
    }
}
