package bf.multi.server.controller;

import bf.multi.server.domain.dto.helpee.HelpeeRequestWriteDto;
import bf.multi.server.domain.dto.websocket.HelpeePingDto;
import bf.multi.server.domain.dto.websocket.HelperPingDto;
import bf.multi.server.domain.dto.websocket.Location;
import bf.multi.server.domain.dto.websocket.MatchingDataDto;
import bf.multi.server.domain.helps.HelpsRepository;
import bf.multi.server.domain.requests.RequestsRepository;
import bf.multi.server.domain.user.User;
import bf.multi.server.service.GeoService;
import bf.multi.server.service.UserService;
import bf.multi.server.service.websocket.GomdoriService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "GET API 모음", description = "메인 화면에서 필요한 DATA GET 요청 API 묶음")
public class ApiController {

    private final HelpsRepository helpsRepository;
    private final RequestsRepository requestsRepository;
    private final GomdoriService gomdoriService;
    private final UserService userService;
    private final GeoService geoService;

    @Tag(name = "GET API 모음")
    @Operation(summary = "곰발바닥 눌렀을 때 DATA",
            description = "Header 설정(value 넣을 때 Beaer 다음에 띄어쓰기 한칸 중요!) : [Authorization]:[Bearer {기본 회원 가입 시 받은 JWT}]" +
                    "\ntoken 줄 때 [곰발바닥] 핑에 있는 JWT를 줘야함")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Helper 핑 데이터 return", content = @Content(schema = @Schema(implementation = HelperPingDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 만료 OR Authorization 설정 에러")})
    @GetMapping("/helper/ping") // 베프의 핑을 눌렀을 때 보이는 정보
    public HelperPingDto getHelperPingInfo() {
        return gomdoriService.responseHelperPing();
    }

    @Tag(name = "GET API 모음")
    @Operation(summary = "곰돌이 눌렀을 때 DATA",
            description = "Header 설정(value 넣을 때 Beaer 다음에 띄어쓰기 한칸 중요!) : [Authorization]:[Bearer {기본 회원 가입 시 받은 JWT}]" +
                          "\ntoken 줄 때 [곰발바닥] 핑에 있는 JWT를 줘야함")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Helpee 핑 데이터 return", content = @Content(schema = @Schema(implementation = HelpeePingDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 만료 OR Authorization 설정 에러") })
    @GetMapping("/helpee/ping") // 곰돌이가 도움 요청 해놓은 핑 눌렀을 때 보이는 정보
    public HelpeePingDto getHelpeePingInfo(HttpServletRequest httpServletRequest){
        return gomdoriService.responseHelpeePing(httpServletRequest);
    }

    @Tag(name = "GET API 모음")
    @Operation(summary = "[베:프] : ?명, [곰돌이] : ?명", description = "메인 화면에 현재 곰발바닥, 곰돌이 각각 몇명 씩 활동중인지 send")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "곰발바닥(베:프), 곰돌이 수 return", content = @Content(schema = @Schema(implementation = UserNum.class))),
            @ApiResponse(responseCode = "404", description = "/connect/users 이 path를 실수하진 않겠지?") })
    @GetMapping("/connect/users") // 현재 접속자 수
    public UserNum countBfAndGomdori(){
        long bf = helpsRepository.findAllBySuccessIsFalse().stream().count();
        long gomdori = requestsRepository.findAllByCompleteIsFalse().stream().count();
        return UserNum.builder().bf(bf).gomdori(gomdori).build();
    }
    @Data
    static class UserNum{
        long bf;
        long gomdori;
        @Builder
        public UserNum(long bf, long gomdori) {
            this.bf = bf;
            this.gomdori = gomdori;
        }
    }

    @Tag(name = "GET API 모음")
    @Operation(summary = "곰돌이 눌렀을 때 DATA",
            description = "[주의-헷갈릴여지가 많음] - 기본 전제: header 에 Authorization,token 두개가 있어야함" +
                    "\nHelper 입장: " +
                    "\n[Authorization]:[Bearer {본인이 가지고 있는 JWT}]" +
                    "\n[token]:[Bearer {요청을 수락했을 때 해당 핑에 담겨있는 helpRequeset의 helpeeJwt}]" +
                    "\n\nHelpee 입장" +
                    "\n[Authorization]:[Bearer {본인이 가지고 있는 JWT}]" +
                    "\n[token]:[Bearer {요청을 수락되었음을 인지했을 때 해당 메세지에 담겨있는 JWT}]"+
                    "\n※추가설명※ Helper, Helpee 두명 모두에게 동일한 데이터 return 됨"
                    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Helpee, Helper 위치 데이터 return", content = @Content(schema = @Schema(implementation = MatchingDataDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 만료 OR Authorization,token 세팅 에러")})
    @GetMapping("/accept") // 도움 주기/받기 매칭 됐을 때
    public MatchingDataDto matching(HttpServletRequest request) {
        String firstBearerToken = request.getHeader("Authorization")
                .substring(7, request.getHeader("Authorization").length());
        String secondBearerToken = request.getHeader("token")
                .substring(7, request.getHeader("token").length());
        return gomdoriService.generateMatchingData(firstBearerToken, secondBearerToken);
    }

    @Tag(name = "GET API 모음")
    @Operation(summary = "요청서에 필요한 데이터 [프로필이미지, 이름, 현재 위치 변환 값",
            description = "Header 설정(value 넣을 때 Beaer 다음에 띄어쓰기 한칸 중요!) : [Authorization]:[Bearer {기본 회원 가입 시 받은 JWT}]"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "HelpeeRequestWriteDto return", content = @Content(schema = @Schema(implementation = HelpeeRequestWriteDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 만료 OR Authorization,token 세팅 에러")})
    @PostMapping("/user/semi/info")
    public HelpeeRequestWriteDto helpeeRequestWriteDto(@RequestBody Location location) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("username: " + userDetails.getUsername() + "password: " + userDetails.getPassword());
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        return HelpeeRequestWriteDto.builder()
                .image(user.getPhotoLink())
                .name(user.getUsername())
                .location(geoService.reverseGeocoding(location.getY(), location.getX()))
                .build();
    }


}
