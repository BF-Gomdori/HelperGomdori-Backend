package bf.multi.server.domain.helps;

import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.helper.HelperRepository;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.requests.RequestsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@SpringBootTest
@Transactional
class HelpsRepositoryTest {

    @Autowired
    RequestsRepository requestsRepository;

    @Autowired
    HelperRepository helperRepository;

    @Autowired
    HelpsRepository helpsRepository;

    @Test
    @DisplayName("Helps에 저장 및 조인하여 Helper, Request와의 연동 테스트")
    public void 도움저장_불러오기() {
        Helper helper = helperRepository.findByUser_Email("example@gmail.com").orElseThrow();
        Requests requests = requestsRepository.findAllByHelpee_User_Email("example@gmail.com").get(0);
        Timestamp acceptTime = new Timestamp(System.currentTimeMillis());
        Timestamp finishTime = new Timestamp(System.currentTimeMillis());
        String success = "Y";
        Double helperRate = 2.6;
        Double helpeeRate = 2.5;
        String helperMessage = "도와드렸습니다.";
        String helpeeMessage = "도움받았습니다.";

        helpsRepository.save(Helps.builder().helper(helper)
                .requests(requests).acceptTime(acceptTime)
                .finishTime(finishTime).success(success)
                .helperRate(helperRate).helpeeRate(helpeeRate)
                .helperMessage(helperMessage).helpeeMessage(helpeeMessage)
                .build());

        Assertions.assertEquals(helperMessage, helpsRepository.findAllByHelper_Id(1L).get(0).getHelperMessage());
        Assertions.assertEquals(acceptTime, helpsRepository.findAllByRequests_Helpee_User_Email("example@gmail.com").get(0).getAcceptTime());
        Assertions.assertEquals(finishTime, helpsRepository.findAllByRequests_Helpee_Id(2L).get(0).getFinishTime());
        Assertions.assertEquals(helpeeMessage, helpsRepository.findAllByHelper_User_Email("example@gmail.com").get(0).getHelpeeMessage());
    }

    @Test
    @DisplayName("Helper 쪽, Request 쪽에서 조회하기 테스트")
    public void 양방향키_확인() {
        Helper helper = helperRepository.findByUser_Email("example@gmail.com").orElseThrow();
        Requests requests = requestsRepository.findAllByHelpee_User_Email("example@gmail.com").get(0);
        Timestamp acceptTime = new Timestamp(System.currentTimeMillis());
        Timestamp finishTime = new Timestamp(System.currentTimeMillis());
        String success = "Y";
        Double helperRate = 2.6;
        Double helpeeRate = 2.5;
        String helperMessage = "도와드렸습니다.";
        String helpeeMessage = "도움받았습니다.";

        Helps helps = Helps.builder().helper(helper)
                .requests(requests).acceptTime(acceptTime)
                .finishTime(finishTime).success(success)
                .helperRate(helperRate).helpeeRate(helpeeRate)
                .helperMessage(helperMessage).helpeeMessage(helpeeMessage)
                .build();
        helps.changeHelper();
        helps.changeRequests();

        helpsRepository.save(helps);

        Assertions.assertEquals(helperRepository.findByUser_Email("example@gmail.com")
                .orElseThrow().getHelpsList().get(0), helpsRepository
                .findAllByHelper_User_Email("example@gmail.com")
                .get(0));

        Assertions.assertEquals(requestsRepository.findAllByHelpee_User_Email("example@gmail.com")
                .get(0).getHelpsList().get(0), helpsRepository
                .findAllByHelper_User_Email("example@gmail.com")
                .get(0));
    }

}