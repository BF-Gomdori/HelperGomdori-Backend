package bf.multi.server.controller;

import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.requests.RequestsRepository;
import bf.multi.server.domain.user.User;
import bf.multi.server.service.UserService;
import bf.multi.server.websocket.domain.HelpRequestDto;
import bf.multi.server.websocket.domain.HelpeePingDto;
import bf.multi.server.websocket.domain.HelperPingDto;
import io.swagger.annotations.Api;
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
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final RequestsRepository requestsRepository;

    // 베프의 핑을 눌렀을 때 보이는 정보
    @GetMapping("/helper/ping")
    public HelperPingDto getHelperPingInfo(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        Helper helper = userService.loadHelperByEncodedEmail(userDetails.getPassword());
        return new HelperPingDto(user, helper);
    }

    // 곰돌이가 도움 요청 해놓은 핑 눌렀을 때 보이는 정보
    @GetMapping("/helpee/ping")
    public HelpeePingDto getHelpeePingInfo(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        Helpee helpee = userService.loadHelpeeByEncodedEmail(userDetails.getPassword());
        Requests requests = requestsRepository.findDistinctTopByHelpeeOrderByRequestTimeDesc(helpee);
        return HelpeePingDto.builder()
                .name(user.getUsername())
                .photoLink(user.getPhotoLink())
                .type(helpee.getType())
                .helpRequestDto(HelpRequestDto.builder()
                        .detailLocation(requests.getLocation())
                        .requestDetail(requests.getReqDetail())
                        .requestType(requests.getReqType())
                        .build())
                .build();
    }

}
