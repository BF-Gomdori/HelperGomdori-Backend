package bf.multi.server.config;

import bf.multi.server.security.JwtAccessDeniedHandler;
import bf.multi.server.security.JwtAuthenticationEntrypoint;
import bf.multi.server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntrypoint jwtAuthenticationEntrypoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(Arrays.asList("*"));
                    cors.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
                    cors.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
                    cors.setAllowCredentials(true);
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", cors);
                    return cors;
                });
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()  // CORS, Session 인증, CSRF 미사용
                .formLogin()
                .disable()  // Form 로그인 미사용
                .httpBasic()
                .disable();  // HTTP basic auth(브라우저기반) 미사용

        http
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/swagger-ui/**", // swagger 관련 정적 파일을 모두 불러와야하기 때문에 /** 필수
                        "/error",
                        "/auth/**")
                .permitAll()    // auth, Oauth, 기타 asset 은 인증없이 접근허용
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // CORS 허용
                .anyRequest().authenticated();    // 그 외 요청은 인증필요

        // add JWT entrypoint & handler
        http
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntrypoint);

        // add JWT adapter configuration before UsernamePasswordAuthenticationFilter
        http.apply(new JwtFilterAdapterConfig(jwtTokenProvider));

        return http.build();
    }

}
