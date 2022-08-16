package bf.multi.server.controller;

import bf.multi.server.domain.dto.JwtTokenDto;
import bf.multi.server.domain.dto.UserDto;
import bf.multi.server.domain.dto.UserLoginDto;
import bf.multi.server.domain.dto.UserSignUpDto;
import bf.multi.server.domain.user.User;
import bf.multi.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody UserLoginDto loginDto) {
        JwtTokenDto tokenDto = authService.loginUser(loginDto);

        if (Objects.isNull(tokenDto)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Authorization header 에 JWT 토큰 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenDto.getToken());

        return new ResponseEntity<>(tokenDto, headers, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public UserDto userSignUp(@Valid @RequestBody UserSignUpDto signUpDto) {
        User user = authService.registerUser(signUpDto);
        return UserDto.from(user);
    }
}
