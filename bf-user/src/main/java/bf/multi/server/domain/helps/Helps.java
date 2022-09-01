package bf.multi.server.domain.helps;

import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.dto.websocket.MessageDto;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@ToString(exclude = {"helper","requests"})
@Entity
@Table(name = "HELPS")
public class Helps {
    /* Helper가 Requests를 수락하여 도와준 정보를 담는 객체 */
    /* ID, HELPER_ID, REQUEST_ID, ACCEPT_TIME, FINISH_TIME, SUCCESS, */
    /* HELPER_RATE, HELPEE_RATE, HELPER_MSG, HELPEE_MSG */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "HELPER_ID", nullable = false)
    private Helper helper;

    @Column(name = "HELPS_JWT")
    private String helpsJwt;

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID")
    private Requests requests;

    @Column(name = "X")
    private String x;

    @Column(name = "Y")
    private String y;

    @Column(name = "ACCEPT_TIME", columnDefinition = "TIMESTAMP")
    private Timestamp acceptTime;

    @Column(name = "FINISH_TIME", columnDefinition = "TIMESTAMP")
    private Timestamp finishTime;

    @Column(name = "SUCCESS", length = 1, nullable = false)
    private boolean success;

    @Column(name = "HELPER_RATE", nullable = false)
    private Double helperRate; // TODO: 기본값 2.5로 지정 필요

    @Column(name = "HELPEE_RATE", nullable = false)
    private Double helpeeRate; // TODO: 기본값 2.5로 지정 필요

    @Column(name = "HELPER_MSG", columnDefinition = "TEXT")
    private String helperMessage;

    @Column(name = "HELPEE_MSG", columnDefinition = "TEXT")
    private String helpeeMessage;

    @Builder
    public Helps(Helper helper, Requests requests, String helpsJwt,
                 String x, String y,
                 Timestamp acceptTime, Timestamp finishTime,
                 boolean success,
                 Double helperRate, Double helpeeRate,
                 String helperMessage, String helpeeMessage) {
        this.helper = helper;
        this.requests = requests;
        this.helpsJwt = helpsJwt;
        this.x = x;
        this.y = y;
        this.acceptTime = acceptTime;
        this.finishTime = finishTime;
        this.success = success;
        this.helperRate = helperRate;
        this.helpeeRate = helpeeRate;
        this.helperMessage = helperMessage;
        this.helpeeMessage = helpeeMessage;
    }

    /* 연관 관계 편의 메서드 */
    public void changeHelper() {
        helper.getHelpsList().add(this);
    }

    public void changeRequests() {
        requests.getHelpsList().add(this);
    }

    public void updateHelps(MessageDto messageDto, Requests requests){
        this.requests = requests;
        this.acceptTime = new Timestamp(System.currentTimeMillis());
        this.success = true;
    }
}
