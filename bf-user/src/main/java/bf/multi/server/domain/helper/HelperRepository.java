package bf.multi.server.domain.helper;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HelperRepository extends JpaRepository<Helper, Long> {
    Optional<Helper> findByUser_Email(String email);

    Optional<Helper> findHelperByUser_Email(String email);
    Optional<Helper> findHelperByUser_Username(String username);
    void deleteByUser_Email(String email);
}