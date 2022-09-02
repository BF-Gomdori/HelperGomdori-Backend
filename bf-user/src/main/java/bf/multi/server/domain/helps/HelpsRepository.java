package bf.multi.server.domain.helps;

import bf.multi.server.domain.helper.Helper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface HelpsRepository extends JpaRepository<Helps, Long> {
    List<Helps> findAllByRequests_Helpee_Id(Long id);

    List<Helps> findAllByHelper_Id(Long id);

    List<Helps> findAllByRequests_Helpee_User_Email(String email);

    List<Helps> findAllByHelper_User_Email(String email);
    List<Helps> findAllBySuccessIsFalse();
    Helps findAllBySuccessIsFalseAndHelper_User_Username(String username);
    Helps findDistinctTopBySuccessIsFalseAndHelper_User_UsernameOrderByAcceptTimeDesc(String username);
    Helps findDistinctFirstByHelperOrderByAcceptTimeDesc(Optional<Helper> helper);

    List<Helps> findAllBySuccessIsFalseAndAcceptTimeBefore(Timestamp timestamp);

    void deleteAllByHelper_User_Email(String email);
}
