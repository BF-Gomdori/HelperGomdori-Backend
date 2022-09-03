package bf.multi.server.controller;

import bf.multi.server.domain.dto.helpee.HelpeeResponseDto;
import bf.multi.server.domain.dto.helpee.HelpeeSignUpDto;
import bf.multi.server.domain.dto.helper.HelperResponseDto;
import bf.multi.server.domain.dto.helper.HelperSignUpDto;
import bf.multi.server.domain.dto.user.JwtTokenDto;
import bf.multi.server.domain.dto.user.KakaoLoginDto;
import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.helper.Helper;
import bf.multi.server.service.AuthService;
import bf.multi.server.service.KakaoUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "회원 가입", description = "KakaoUser, Helper, Helpee 회원 가입 API")
public class AuthController {
    private final KakaoUserService kakaoUserService;
    private final AuthService authService;

    @Tag(name = "회원 가입")
    @Operation(summary = "기본 회원가입 및 로그인 처리", description = "카카오 access_token, FCMToken 및 기타 정보를 받아서 JWT 발행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User 등록 및 로그인 성공", content = @Content(schema = @Schema(implementation = JwtTokenDto.class))),
            @ApiResponse(responseCode = "401", description = "카카오 access_token 만료"),
            @ApiResponse(responseCode = "500", description = "이미 회원 가입되어 있음"),})
    @PostMapping("/barrierfree")
    public JwtTokenDto bfRegister(@RequestBody KakaoLoginDto kakaoLoginDto, HttpServletResponse response)
            throws JsonProcessingException {
        return kakaoUserService.kakaoRegister(kakaoLoginDto, response);
    }

    @Tag(name = "회원 가입")
    @Operation(summary = "Helpee(도움받기)로 등록",
            description = "Header 설정(value 넣을 때 Bearer 다음에 띄어쓰기 한칸 중요!) : [Authorization]:[Bearer {기본 회원 가입 시 받은 JWT}]" +
                    "\nBody : type, intro 필요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Helpee 등록 성공"),
            @ApiResponse(responseCode = "401", description = "JWT 만료 OR Authorization 설정 에러") })
    @PostMapping("/signup/helpee")
    public void helpeeSignup(
            @RequestBody HelpeeSignUpDto.SignUpInfo signUpInfo) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HelpeeSignUpDto helpeeSignUpDto = HelpeeSignUpDto.builder().signUpInfo(signUpInfo).build();
        helpeeSignUpDto.setUser(authService.getUserByPassword(userDetails.getPassword()));
        Helpee helpee = authService.connectHelpee(helpeeSignUpDto);

        log.info(HelpeeResponseDto.from(helpee).toString());
    }

    @Tag(name = "회원 가입")
    @Operation(summary = "Helper(도움주기)로 등록",
            description = "Header 설정(value 넣을 때 Beaer 다음에 띄어쓰기 한칸 중요!) : [Authorization]:[Bearer {기본 회원 가입 시 받은 JWT}]" +
                          "\nBody : POST지만 body설정 안해도됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Helper 등록 성공"),
            @ApiResponse(responseCode = "401", description = "JWT 만료 OR Authorization 설정 에러") })
    @PostMapping("/signup/helper")
    public void helperSignup() {
        HelperSignUpDto helperSignUpDto = new HelperSignUpDto();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        helperSignUpDto.setUser(authService.getUserByPassword(userDetails.getPassword()));
        Helper helper = authService.connectHelper(helperSignUpDto);

        log.info(HelperResponseDto.from(helper).toString());
    }

}
