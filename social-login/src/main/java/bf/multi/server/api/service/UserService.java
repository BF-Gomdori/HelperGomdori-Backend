package bf.multi.server.api.service;

import bf.multi.server.api.entity.user.User;
import bf.multi.server.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String userId){
        return userRepository.findByUserId(userId);
    }
}
