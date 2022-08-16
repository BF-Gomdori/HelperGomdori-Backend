package bf.multi.server.service;

import bf.multi.server.domain.dto.JwtTokenDto;
import bf.multi.server.domain.dto.UserLoginDto;
import bf.multi.server.domain.dto.UserSignUpDto;
import bf.multi.server.domain.user.User;
import bf.multi.server.exception.UserAlreadyExistsException;
import bf.multi.server.repository.UserRepository;
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

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenDto loginUser(UserLoginDto loginDto) {
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
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .build();

            return userRepository.save(newUser);
        }
    }
}
