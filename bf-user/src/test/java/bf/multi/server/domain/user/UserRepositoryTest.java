package bf.multi.server.domain.user;

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

//    @AfterAll
//    public void cleanup(){
//        userRepository.deleteAll();
//    }

    @Test
    @DisplayName("유저 정보가 잘 저장되고 잘 불러와지는지 테스트~")
    public void 저장_불러오기() {
        String name = "김민근";
        String email = "example@gmail.com";
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

        System.out.println(userList);

    }
}