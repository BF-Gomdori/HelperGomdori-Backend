package bf.multi.server.controller;

import bf.multi.server.domain.User;
import bf.multi.server.jwt.JwtTokenProvider;
import bf.multi.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    User user = User.builder()
            .userEmail("test")
            .userName("승민")
            .userSequenceId(Long.valueOf(1))
            .build();

    @PostMapping("/test")
    public String test(){
        return "통과";
    }

    @PostMapping("/join")
    public String join(){
        log.info("로그인 시도됨");
        userRepository.save(user);
        return user.toString();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        log.info("user email = {}", user.get("email"));
        User member = userRepository.findByUserEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        return jwtTokenProvider.createToken(member.getUserEmail(), member.getRoles());
    }

}
