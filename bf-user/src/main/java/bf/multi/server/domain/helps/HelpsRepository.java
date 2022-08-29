package bf.multi.server.domain.helps;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpsRepository extends JpaRepository<Helps, Long> {
    List<Helps> findAllByRequests_Helpee_Id(Long id);

    List<Helps> findAllByHelper_Id(Long id);

    List<Helps> findAllByRequests_Helpee_User_Email(String email);

    List<Helps> findAllByHelper_User_Email(String email);
    List<Helps> findAllBySuccessIsFalse();
    Helps findAllBySuccessIsFalseAndHelper_User_Username(String username);

}
