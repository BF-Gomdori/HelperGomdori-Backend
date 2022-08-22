package bf.multi.server.domain.helps;

import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.requests.Requests;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@ToString
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

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "HELPER_ID", nullable = false)
    private Helper helper;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "REQUEST_ID", nullable = false)
    private Requests requests;

    @Column(name = "ACCEPT_TIME", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp acceptTime;

    @Column(name = "FINISH_TIME", columnDefinition = "TIMESTAMP")
    private Timestamp finishTime;

    @Column(name = "SUCCESS", length = 1, nullable = false)
    private String success;

    @Column(name = "HELPER_RATE", nullable = false)
    private Double helperRate; // TODO: 기본값 2.5로 지정 필요

    @Column(name = "HELPEE_RATE", nullable = false)
    private Double helpeeRate; // TODO: 기본값 2.5로 지정 필요

    @Column(name = "HELPER_MSG", columnDefinition = "TEXT")
    private String helperMessage;

    @Column(name = "HELPEE_MSG", columnDefinition = "TEXT")
    private String helpeeMessage;
}