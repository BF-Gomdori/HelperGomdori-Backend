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
                .cors()
                .and()
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
                        "/error",
                        "/auth/**")
                .permitAll()    // auth, Oauth, 기타 asset 은 인증없이 접근허용
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
