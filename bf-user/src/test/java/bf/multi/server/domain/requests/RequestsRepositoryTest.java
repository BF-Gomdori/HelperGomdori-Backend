//package bf.multi.server.domain.requests;
//
//import bf.multi.server.domain.helpee.Helpee;
//import bf.multi.server.domain.helpee.HelpeeRepository;
//import bf.multi.server.domain.user.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.transaction.Transactional;
//import java.sql.Timestamp;
//
//@SpringBootTest
//@Transactional
//class RequestsRepositoryTest {
//
//    @Autowired
//    RequestsRepository requestsRepository;
//
//    @Autowired
//    HelpeeRepository helpeeRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    @DisplayName("Requests 저장 및 Helpee 테이블과의 연동 테스트")
//    public void 저장_불러오기() {
//
//        Helpee helpee = helpeeRepository.findById(userRepository
//                .findByEmail("example@gmail.com")
//                .orElseThrow().getHelpee().getId()
//        ).orElseThrow();
//
//        String message = "도와주세요~";
//        String location = "숭실대입구역 2번 출구";
//        Timestamp requestTime = new Timestamp(System.currentTimeMillis());
//
//        Requests requests = Requests.builder()
//                .helpee(helpee).reqType("계단/리프트").reqDetail(message)
//                .location(location).requestTime(requestTime)
//                .build();
//        requests.changeHelpee();
//
//        requestsRepository.save(requests);
//
////        Assertions.assertEquals(message, requests.getMessage());
//        Assertions.assertEquals(helpee.getUser().getUsername(), requests.getHelpee().getUser().getUsername());
//        Assertions.assertEquals(helpeeRepository.findById(userRepository
//                        .findByEmail("example@gmail.com")
//                        .orElseThrow().getHelpee().getId())
//                .orElseThrow().getRequestsList().get(0)
//                .getLocation(), requestsRepository
//                .findAllByHelpee_User_Email(helpee.getUser().getEmail())
//                .get().getLocation()
//        );
//
//    }
//}