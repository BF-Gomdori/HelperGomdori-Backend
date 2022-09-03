package bf.multi.server.service;

import bf.multi.server.domain.helps.HelpsRepository;
import bf.multi.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class HelpsService {

    private final HelpsRepository helpsRepository;

    private final UserRepository userRepository;

    public void deleteHelpsByEncodedEmail(String encodedEmail) {
        String email = userRepository.findByPassword(encodedEmail).orElseThrow().getEmail();
        helpsRepository.deleteAllByHelper_User_Email(email);
    }
}
