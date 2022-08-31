package bf.multi.server.service;


import bf.multi.server.domain.dto.helpee.HelpeeSignUpDto;
import bf.multi.server.domain.dto.helper.HelperSignUpDto;
import bf.multi.server.domain.dto.user.JwtTokenDto;
import bf.multi.server.domain.dto.user.UserLoginDto;
import bf.multi.server.domain.dto.user.UserSignUpDto;
import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.helpee.HelpeeRepository;
import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.helper.HelperRepository;
import bf.multi.server.domain.user.User;
import bf.multi.server.domain.dto.user.*;
import bf.multi.server.domain.user.UserRole;
import bf.multi.server.exception.UserAlreadyExistsException;
import bf.multi.server.domain.user.UserRepository;
import bf.multi.server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final HelpeeRepository helpeeRepository;

    private final HelperRepository helperRepository;

    public JwtTokenDto loginUser(UserLoginDto loginDto) {
        log.info("username: " + loginDto.getUsername() + " || password: " + loginDto.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // JWT token 발급
            String jwtToken = jwtTokenProvider.generateToken(authentication);
            return JwtTokenDto.builder()
                    .token(jwtToken)
                    .build();

        } catch (AuthenticationException e) {
            log.warn("User Authentication fail: loginDTO {} | {}", loginDto, e.getMessage());
        } return null;
    }

    @Transactional(readOnly = false)
    public User registerUser(UserSignUpDto signUpDto) throws UserAlreadyExistsException {

        Optional<User> existingUser = userRepository.findByEmail(signUpDto.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + signUpDto.getEmail());
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            User newUser = User.builder()
                    .username(signUpDto.getUsername())
                    .email(signUpDto.getEmail())
                    .password(passwordEncoder.encode(signUpDto.getEmail()))
                    .photoLink(signUpDto.getPhotoLink())
                    .gender(signUpDto.getGender())
                    .phone(signUpDto.getPhone())
                    .age(signUpDto.getAge())
                    .startDate(new Timestamp(System.currentTimeMillis()))
                    .modifiedDate(new Timestamp(System.currentTimeMillis()))
                    .role(UserRole.ROLE_USER) // 일단은 무조건 USER 권한 주는 걸로
                    .build();

            return userRepository.save(newUser);
        }
    }

    @Transactional
    public Helpee connectHelpee(HelpeeSignUpDto helpeeSignUpDto) {
        Helpee helpee = helpeeSignUpDto.toEntity();

        return helpeeRepository.save(helpee);
    }

    @Transactional
    public Helper connectHelper(HelperSignUpDto helperSignUpDto) {
        Helper helper = helperSignUpDto.toEntity();

        return helperRepository.save(helper);
    }

    @Transactional
    public User getUserByPassword(String password) {
        return userRepository.findByPassword(password).orElseThrow();
    }
}
