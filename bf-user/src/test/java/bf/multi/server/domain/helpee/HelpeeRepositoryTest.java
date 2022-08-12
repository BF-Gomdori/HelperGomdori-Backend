package bf.multi.server.domain.helpee;

import bf.multi.server.domain.user.User;
import bf.multi.server.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
//@Transactional
class HelpeeRepositoryTest {

    @Autowired
    HelpeeRepository helpeeRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("헬피 정보 저장 불러오기 및 유저 테이블과의 연동 테스트")
    public void 저장_불러오기() {
        User user = userRepository.findByEmail("example@gmail.com").orElseThrow();
        String type = "지체";
        Integer requestCount = 112;
        Double averageRate = 3.5;
        Integer hearts = 72;

        helpeeRepository.save(Helpee
                .builder()
                .user(user)
                .type(type)
                .requestCount(requestCount)
                .averageRate(averageRate)
                .hearts(hearts)
                .build()
        );

        List<Helpee> helpeeList = helpeeRepository.findAll();

        Assertions.assertEquals(helpeeList.get(0).getAverageRate(), averageRate);
        Assertions.assertEquals(helpeeList.get(0).getUser().getName(), user.getName());
        Assertions.assertEquals(helpeeList.get(0).getId(), userRepository.
                findByEmail("example@gmail.com").orElseThrow().
                getHelpee().getId());

    }
}