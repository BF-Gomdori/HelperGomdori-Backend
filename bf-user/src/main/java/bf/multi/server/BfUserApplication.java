package bf.multi.server;

import bf.multi.server.domain.user.User;
import bf.multi.server.domain.user.UserRole;
import bf.multi.server.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class BfUserApplication {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public BfUserApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostConstruct
	public void initUsers() {
		userRepository.findByUsername("admin").orElseGet(() -> {
			User user = User.builder()
					.username("admin")
					.email("admin@social.com")
					.password(passwordEncoder.encode("test1234"))
					.role(UserRole.ROLE_ADMIN)
					.build();

			userRepository.save(user);
			return user;
		});
	}

	public static void main(String[] args) {
		SpringApplication.run(BfUserApplication.class, args);
	}

}
