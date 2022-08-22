package bf.multi.server.controller;

import bf.multi.server.domain.dto.UserDto;
import bf.multi.server.domain.user.User;
import bf.multi.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/self")
    public User userSelfDetail() {
        // TODO: SecurityContextHolder 에서 유저 정보 조회
        // https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("username: "+userDetails.getUsername()+"password: "+userDetails.getPassword());
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        return user;
    }
}
