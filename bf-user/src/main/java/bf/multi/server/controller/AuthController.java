package bf.multi.server.controller;

import bf.multi.server.domain.dto.helpee.HelpeeResponseDto;
import bf.multi.server.domain.dto.helpee.HelpeeSignUpDto;
import bf.multi.server.domain.dto.helper.HelperResponseDto;
import bf.multi.server.domain.dto.helper.HelperSignUpDto;
import bf.multi.server.domain.dto.user.*;
import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.user.User;
import bf.multi.server.service.AuthService;
import bf.multi.server.service.KakaoUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

@Api
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final KakaoUserService kakaoUserService;
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

    @PostMapping("/barrierfree")
    public JwtTokenDto bfRegister(
            @RequestBody KakaoLoginDto kakaoLoginDto,
            HttpServletResponse response) throws JsonProcessingException {
        return kakaoUserService.kakaoRegister(kakaoLoginDto, response);
    }

    @PostMapping("/signup/helpee")
    public HelpeeResponseDto helpeeSignup(
            @RequestBody String type) {
        HelpeeSignUpDto helpeeSignUpDto = HelpeeSignUpDto.builder().type(type).build();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        helpeeSignUpDto.setUser(authService.getUserByPassword(userDetails.getPassword()));
        Helpee helpee = authService.connectHelpee(helpeeSignUpDto);

        return HelpeeResponseDto.from(helpee);
    }


    @PostMapping("/signup/helper")
    public HelperResponseDto helperSignup() {
        HelperSignUpDto helperSignUpDto = new HelperSignUpDto();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        helperSignUpDto.setUser(authService.getUserByPassword(userDetails.getPassword()));
        Helper helper = authService.connectHelper(helperSignUpDto);

        return HelperResponseDto.from(helper);
    }

}
