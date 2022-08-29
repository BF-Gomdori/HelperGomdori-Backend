package bf.multi.server.service;

import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.requests.RequestsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class RequestsService {

    private final RequestsRepository requestsRepository;

    public Requests loadRecentByHelpee(Helpee helpee) {
        return requestsRepository.findDistinctTopByHelpeeOrderByRequestTimeDesc(helpee);
    }
}
