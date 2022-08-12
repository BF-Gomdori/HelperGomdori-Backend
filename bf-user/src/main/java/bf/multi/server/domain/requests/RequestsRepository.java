package bf.multi.server.domain.requests;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestsRepository extends JpaRepository<Requests, Long> {
    
}
