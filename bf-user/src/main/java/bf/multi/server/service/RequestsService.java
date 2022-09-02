package bf.multi.server.service;

import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.requests.RequestsRepository;
import bf.multi.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class RequestsService {

    private final UserRepository userRepository;
    private final RequestsRepository requestsRepository;

    public Requests loadRecentByHelpee(Helpee helpee) {
        return requestsRepository.findDistinctTopByHelpeeOrderByRequestTimeDesc(helpee);
    }

    public void deleteRequestsByEncodedEmail(String encodedEmail) {
        String email = userRepository.findByPassword(encodedEmail).orElseThrow().getEmail();
        requestsRepository.deleteAllByHelpee_User_Email(email);
    }
}
