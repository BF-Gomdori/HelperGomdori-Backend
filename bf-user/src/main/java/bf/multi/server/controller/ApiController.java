package bf.multi.server.controller;

import bf.multi.server.domain.helps.HelpsRepository;
import bf.multi.server.domain.requests.RequestsRepository;
import bf.multi.server.websocket.domain.HelpeePingDto;
import bf.multi.server.websocket.domain.HelperPingDto;
import bf.multi.server.websocket.domain.MatchingDataDto;
import bf.multi.server.websocket.service.GomdoriService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final HelpsRepository helpsRepository;
    private final RequestsRepository requestsRepository;

    private final GomdoriService gomdoriService;

    // 베프의 핑을 눌렀을 때 보이는 정보
    @GetMapping("/helper/ping")
    public HelperPingDto getHelperPingInfo() {
        return gomdoriService.responseHelperPing();
    }

    // 곰돌이가 도움 요청 해놓은 핑 눌렀을 때 보이는 정보
    @GetMapping("/helpee/ping")
    public HelpeePingDto getHelpeePingInfo(HttpServletRequest httpServletRequest){
        return gomdoriService.responseHelpeePing(httpServletRequest);
    }

    // 현재 접속자 수
    @GetMapping("/connect/users")
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

    // 도움 주기/받기 매칭 됐을 때
    @GetMapping("/accept")
    public MatchingDataDto matching(HttpServletRequest request){
        String firstBearerToken = request.getHeader("Authorization")
                .substring(7, request.getHeader("Authorization").length());
        String secondBearerToken = request.getHeader("token")
                .substring(7, request.getHeader("token").length());
        return gomdoriService.generateMatchingData(firstBearerToken, secondBearerToken);
    }

}
