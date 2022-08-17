package bf.multi.server.domain.requests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestsRepository extends JpaRepository<Requests, Long> {
    List<Requests> findAllByHelpee_User_Id(Long id);

    List<Requests> findAllByHelpee_User_Email(String email);
}
