package bf.multi.server.service;


import bf.multi.server.domain.dto.user.JwtTokenDto;
import bf.multi.server.domain.dto.user.KakaoLoginDto;
import bf.multi.server.domain.dto.user.KakaoUserInfoDto;
import bf.multi.server.domain.user.User;
import bf.multi.server.domain.user.UserRepository;
import bf.multi.server.domain.user.UserRole;
import bf.multi.server.security.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    /**
     * 1. access-token 받아서 카카오 API 호출
     * 2. 카카오 email로 회원가입 처리
     * 3. 강제 로그인 처리
     * 4. response Header에 JWT 토큰 추가해서 반환
     *
     * @param response
     * @return
     * @throws JsonProcessingException
     */
    public JwtTokenDto kakaoRegister(KakaoLoginDto kakaoLoginDto, HttpServletResponse response) throws JsonProcessingException{
        // 1. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(kakaoLoginDto.getAccessToken());

        // 2. 카카오 EMAIL로 회원가입 처리
        User kakaoUser = registerKakaoUser(kakaoLoginDto, kakaoUserInfo);

        // 3. 강제 로그인 처리
        Authentication authentication = forceLogin(kakaoUser);

        // 4. response Header에 JWT 토큰 추가
        return issueToken(authentication, response);

    }

    // 1. 토큰으로 카카오 API 호출
    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException{
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        log.info(String.valueOf(response));
        // responseBody 정보 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String age = jsonNode.get("kakao_account").get("age_range").asText();
        String gender = jsonNode.get("kakao_account").get("gender").asText();
        String thumbnailImage = jsonNode.get("properties").get("thumbnail_image").asText();

        return new KakaoUserInfoDto(id, email, nickname, age, gender, thumbnailImage);
    }

    // 2. 카카오EMAIL로 회원가입 처리
    private User registerKakaoUser(KakaoLoginDto kakaoLoginDto, KakaoUserInfoDto kakaoUserInfoDto){
        // DB에 중복된 email이 있는지 check
        String kakaoEmail = kakaoUserInfoDto.getEmail();
        User kakaoUser = userRepository.findByEmail(kakaoEmail).orElse(null);

        if(kakaoUser == null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // register
            String encodedEmail = passwordEncoder.encode(kakaoEmail);
            kakaoUser = new User(
                    kakaoLoginDto.getName(),
                    kakaoEmail,
                    encodedEmail,
                    kakaoUserInfoDto.getThumbnailImage(),
                    kakaoUserInfoDto.getGender().equals("male") ? "남":"여",
                    kakaoLoginDto.getPhone(),
                    kakaoUserInfoDto.getAge(),
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()),
                    UserRole.ROLE_USER
            );
            userRepository.save(kakaoUser);
        }
        log.info(kakaoUser.toString());
        return kakaoUser;
    }

    // 3. 강제 로그인 처리
    private Authentication forceLogin(User kakaoUser){
        log.info("username: "+kakaoUser.getUsername()+" || password: "+kakaoUser.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            kakaoUser.getUsername(),
                            kakaoUser.getEmail()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        } catch (AuthenticationException e) {
            log.warn("User Authentication fail: 카카오로그인 실패 {} | {}", kakaoUser, e.getMessage());
        } return null;
    }

    // 4. response Header에 JWT 토큰 추가
    private JwtTokenDto issueToken(Authentication authentication, HttpServletResponse response) {
        // response header에 token 추가
        String jwtToken = jwtTokenProvider.generateToken(authentication);
//        response.addHeader("Authorization","Bearer "+jwtToken);
        return JwtTokenDto.builder()
                .token(jwtToken)
                .build();
    }
}
