package bf.multi.server.controller;

import bf.multi.server.domain.dto.helpee.HelpeeProfileResponseDto;
import bf.multi.server.domain.dto.user.UserProfileResponseDto;
import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.user.User;
import bf.multi.server.service.HelpsService;
import bf.multi.server.service.RequestsService;
import bf.multi.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "User 프로필 조회, 삭제 관련 API 모음")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final RequestsService requestsService;

    private final HelpsService helpsService;

    @GetMapping("/self")
    public User userSelfDetail() {
        // TODO: SecurityContextHolder 에서 유저 정보 조회
        // https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("username: " + userDetails.getUsername() + "password: " + userDetails.getPassword());
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        return user;
    }

    @Tag(name = "User 프로필 조회, 삭제 관련 API 모음")
    @GetMapping("/profile")
    public HelpeeProfileResponseDto helpeeProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        Helpee helpee = userService.loadHelpeeByEncodedEmail(userDetails.getPassword());

        return HelpeeProfileResponseDto
                .builder()
                .userProfileResponseDto(
                        UserProfileResponseDto
                                .builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .photoLink(user.getPhotoLink())
                                .gender(user.getGender())
                                .age(user.getAge())
                                .build()
                )
                .requests(helpee.getRequestsList())
                .build();
    }

    @Tag(name = "User 프로필 조회, 삭제 관련 API 모음")
    @Operation(description = "helpee 삭제 (helpee JWT만 포함하면 됨)")
    @DeleteMapping("/helpee")
    public Boolean helpeeDelete() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            userService.deleteHelpeeByEncodedEmail(userDetails.getPassword());
        } catch (Exception E) {
            throw new RuntimeException("Error in deletion");
        }

        return true;
    }

    @Tag(name = "User 프로필 조회, 삭제 관련 API 모음")
    @Operation(description = "helper 삭제 (Helper JWT만 포함하면 됨)")
    @DeleteMapping("/helper")
    public Boolean helperDelete() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            userService.deleteHelperByEncodedEmail(userDetails.getPassword());
        } catch (Exception E) {
            throw new RuntimeException("Error in deletion");
        }

        return true;
    }

    @Tag(name = "User 프로필 조회, 삭제 관련 API 모음")
    @Operation(description = "user 삭제 (User JWT만 포함하면 됨)")
    @DeleteMapping("/user")
    public Boolean userDelete() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            userService.deleteUserByEncodedEmail(userDetails.getPassword());
        } catch (Exception E) {
            throw new RuntimeException("Error in deletion");
        }

        return true;
    }

    @Tag(name = "User 프로필 조회, 삭제 관련 API 모음")
    @Operation(description = "requests 삭제 (Helpee 입장에서의 JWT만 포함하면 됨)")
    @DeleteMapping("/requests")
    public Boolean requestsDelete() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            requestsService.deleteRequestsByEncodedEmail(userDetails.getPassword());
        } catch (Exception E) {
            throw new RuntimeException("Error in deletion");
        }

        return true;
    }

    @Tag(name = "User 프로필 조회, 삭제 관련 API 모음")
    @Operation(description = "helps 삭제 (Helper 입장에서의 JWT만 포함하면 됨)")
    @DeleteMapping("/helps")
    public Boolean helpsDelete() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            helpsService.deleteHelpsByEncodedEmail(userDetails.getPassword());
        } catch (Exception E) {
            throw new RuntimeException("Error in deletion");
        }

        return true;
    }
}
