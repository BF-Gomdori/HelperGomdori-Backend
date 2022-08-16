package bf.multi.server.domain.helper;

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
class HelperRepositoryTest {

    @Autowired
    HelperRepository helperRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("헬퍼 정보가 잘 저장되고 잘 불러와질까요")
    public void 저장_불러오기() {
        User user = userRepository.findByEmail("example@naver.com").orElseThrow();
        Integer helpCount = 11;
        Double averageRate = 3.5;
        Integer hearts = 72;

        helperRepository.save(Helper
                .builder()
                .user(user)
                .helpCount(helpCount)
                .averageRate(averageRate)
                .hearts(hearts)
                .build()
        );

        List<Helper> helperList = helperRepository.findAll();

        Assertions.assertEquals(helperList.get(1).getAverageRate(), averageRate);
        Assertions.assertEquals(helperList.get(1).getUser().getUsername(), user.getUsername());
        Assertions.assertEquals(helperList.get(1).getId(), userRepository.
                findByEmail("example@naver.com").orElseThrow().
                getHelper().getId());
    }

    @Test
    @DisplayName("findByUser_Email() 메서드 작동 테스트임~")
    public void 이메일로_찾기() {
        Assertions.assertEquals(helperRepository
                .findByUser_Email("example@gmail.com")
                .orElseThrow().getId(), userRepository
                .findByEmail("example@gmail.com")
                .orElseThrow().getId()
        );
    }
}