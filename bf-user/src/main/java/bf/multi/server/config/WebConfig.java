package bf.multi.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://kapi.kakao.com/v2/user/me",
                        "http://localhost:9000")
                .allowedMethods("*")
                .exposedHeaders("X-AUTH_TOKEN")
                .allowCredentials(true)
                .maxAge(3000);
    }
}
