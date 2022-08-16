package bf.multi.server.controller;

import bf.multi.server.domain.dto.UserDto;
import bf.multi.server.domain.user.User;
import bf.multi.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/self")
    public UserDto userSelfDetail() {
        // TODO: SecurityContextHolder 에서 유저 정보 조회
        // https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.loadUserByUsername(userDetails.getUsername());
        return UserDto.from(user);
    }
}
