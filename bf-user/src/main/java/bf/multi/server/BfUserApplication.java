package bf.multi.server;

import bf.multi.server.domain.user.User;
import bf.multi.server.domain.user.UserRepository;
import bf.multi.server.domain.user.UserRole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;

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
					.password(passwordEncoder.encode("admin@social.com"))
					.photoLink("사진")
					.gender("남")
					.phone("010-9134-7564")
					.age("20대 중반")
					.startDate(new Timestamp(System.currentTimeMillis()))
					.modifiedDate(new Timestamp(System.currentTimeMillis()))
					.FCMToken("FCMtoken")
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
