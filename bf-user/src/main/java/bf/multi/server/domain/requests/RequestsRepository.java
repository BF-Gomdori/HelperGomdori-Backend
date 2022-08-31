package bf.multi.server.domain.requests;

import bf.multi.server.domain.helpee.Helpee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<Requests, Long> {
    List<Requests> findAllByHelpee_User_Id(Long id);

//    List<Requests> findAllByHelpee_User_Email(String email);
    Optional<Requests> findAllByHelpee_User_Email(String email);
    Requests findDistinctFirstByHelpeeAndCompleteIsFalse(Helpee helpee);
    Requests findDistinctTopByHelpeeOrderByRequestTimeDesc(Helpee helpee);
    List<Requests> findAllByCompleteIsFalse();
    List<Requests> findAllByCompleteIsFalseAndRequestTimeBefore(Timestamp timestamp);
    Requests findDistinctTopByCompleteIsFalseAndHelpee_User_UsernameOrderByRequestTimeDesc(String username);
}
