package bf.multi.server.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPassword(String password);

    Optional<User> findByUsername(String username);

    void deleteByEmail(String email);
}
