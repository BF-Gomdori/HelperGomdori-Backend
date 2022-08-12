package bf.multi.server.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("유저 정보가 잘 저장되고 잘 불러와지는지 테스트~")
    public void 저장_불러오기() {
        String name = "김민근";
        String email = "example@gmail2.com";
        String photoLink = "www.example.com";
        String gender = "M";
        String phone = "01000000000";
        Integer age = 24;
        String intro = "자기소개입니다.";
        Timestamp startDate = new Timestamp(System.currentTimeMillis());
        Timestamp modifiedDate = new Timestamp(System.currentTimeMillis());


        userRepository.save(
                User.builder()
                        .name(name)
                        .email(email)
                        .photoLink(photoLink)
                        .gender(gender)
                        .phone(phone)
                        .age(age)
                        .intro(intro)
                        .startDate(startDate)
                        .modifiedDate(modifiedDate)
                        .build()
        );

        List<User> userList = userRepository.findAll();

        User user = userList.get(1);
        Assertions.assertEquals(user.getEmail(), email);
        Assertions.assertEquals(user.getIntro(), intro);

        Assertions.assertEquals(userRepository
                .findByEmail(email).orElseThrow()
                .getName(), name);


    }
}