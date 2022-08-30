package bf.multi.server.controller;

import bf.multi.server.domain.dto.helpee.HelpeeResponseDto;
import bf.multi.server.domain.dto.helpee.HelpeeSignUpDto;
import bf.multi.server.domain.dto.helper.HelperResponseDto;
import bf.multi.server.domain.dto.helper.HelperSignUpDto;
import bf.multi.server.domain.dto.user.*;
import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.helper.Helper;
import bf.multi.server.service.AuthService;
import bf.multi.server.service.KakaoUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final KakaoUserService kakaoUserService;
    private final AuthService authService;

    @PostMapping("/barrierfree")
    public JwtTokenDto bfRegister(
            @RequestBody KakaoLoginDto kakaoLoginDto,
            HttpServletResponse response) throws JsonProcessingException {
        return kakaoUserService.kakaoRegister(kakaoLoginDto, response);
    }

    @PostMapping("/signup/helpee")
    public void helpeeSignup(
            @RequestBody HelpeeSignUpDto helpeeSignUpDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        helpeeSignUpDto.setUser(authService.getUserByPassword(userDetails.getPassword()));
        Helpee helpee = authService.connectHelpee(helpeeSignUpDto);

        log.info(HelpeeResponseDto.from(helpee).toString());
    }


    @PostMapping("/signup/helper")
    public void helperSignup() {
        HelperSignUpDto helperSignUpDto = new HelperSignUpDto();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        helperSignUpDto.setUser(authService.getUserByPassword(userDetails.getPassword()));
        Helper helper = authService.connectHelper(helperSignUpDto);

        log.info(HelperResponseDto.from(helper).toString());
    }

}
