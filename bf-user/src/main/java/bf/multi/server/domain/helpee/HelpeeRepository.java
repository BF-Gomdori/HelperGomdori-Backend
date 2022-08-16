package bf.multi.server.domain.helpee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HelpeeRepository extends JpaRepository<Helpee, Long> {
    Optional<Helpee> findByUser_Email(String email);
}
