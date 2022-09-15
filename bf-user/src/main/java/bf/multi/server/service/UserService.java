package bf.multi.server.service;

import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.helpee.HelpeeRepository;
import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.helper.HelperRepository;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.user.User;
import bf.multi.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final HelperRepository helperRepository;
    private final HelpeeRepository helpeeRepository;

    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User loadUserByEncodedEmail(String encodedEmail) throws UsernameNotFoundException {
        return userRepository.findByPassword(encodedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + encodedEmail));
    }

    public Helper loadHelperByEncodedEmail(String encodedEmail) throws UsernameNotFoundException {
        String email = userRepository.findByPassword(encodedEmail).get().getEmail();
        Helper helper = helperRepository.findHelperByUser_Email(email)
                .orElseThrow(() -> new UsernameNotFoundException("Helper not found with email: " + encodedEmail));
        return helper;
    }

    public Helpee loadHelpeeByEncodedEmail(String encodedEmail) throws UsernameNotFoundException {
        String email = userRepository.findByPassword(encodedEmail).get().getEmail();
        Helpee helpee = helpeeRepository.findByUser_Email(email)
                .orElseThrow(() -> new UsernameNotFoundException("Helpee not found with email: " + encodedEmail));
        return helpee;
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public User loadUserById(Long id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    public List<Requests> loadRequestsListByEncodedEmail(String encodedEmail) {
        String email = userRepository.findByPassword(encodedEmail).orElseThrow().getEmail();
        Helpee helpee = helpeeRepository.findByUser_Email(email)
                .orElseThrow(() -> new UsernameNotFoundException("Helpee not found with email: " + encodedEmail));
        return helpee.getRequestsList();
    }

    public void deleteHelpeeByEncodedEmail(String encodedEmail) {
        String email = userRepository.findByPassword(encodedEmail).orElseThrow().getEmail();
        helpeeRepository.deleteByUser_Email(email);
    }

    public void deleteHelperByEncodedEmail(String encodedEmail) {
        String email = userRepository.findByPassword(encodedEmail).orElseThrow().getEmail();
        helperRepository.deleteByUser_Email(email);
    }

    public void deleteUserByEncodedEmail(String encodedEmail) {
        String email = userRepository.findByPassword(encodedEmail).orElseThrow().getEmail();
        userRepository.deleteByEmail(email);
    }
}
