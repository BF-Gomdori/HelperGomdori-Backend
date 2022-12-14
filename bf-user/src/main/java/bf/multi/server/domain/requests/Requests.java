package bf.multi.server.domain.requests;

import bf.multi.server.domain.dto.websocket.MessageDto;
import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.helps.Helps;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString(exclude = {"helpee","helpsList"})
@Entity
@Table(name = "REQUESTS")
public class Requests {
    /* Helpee가 요청한 Requests에 관한 정보를 담는 객체 */
    /* ID, HELPEE_ID, MSG, LOCATION, REQUEST_TIME */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "HELPEE_ID", nullable = false)
    private Helpee helpee;

    @Column(name = "REQUESTS_JWT")
    private String requestsJwt;

    @Column(name = "COMPLETE", length = 1, nullable = false)
    private boolean complete;

    @Column(name = "HELP_REQUEST_TYPE", columnDefinition = "TEXT")
    private String requestType;

    @Column(name = "HELP_REQUEST_DETAIL", columnDefinition = "TEXT")
    private String requestDetail;

    @Column(name = "LOCATION", length = 45)
    private String location;

    @Column(name = "X")
    private Double x;

    @Column(name = "Y")
    private Double y;

    @Column(name = "REQUEST_TIME", columnDefinition = "TIMESTAMP")
    private Timestamp requestTime;

    /* 연관 관계 편의 메서드 */
    public void changeHelpee() {
        helpee.getRequestsList().add(this);
    }

    @OneToMany(mappedBy = "requests", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Helps> helpsList = new ArrayList<>();

    @Builder
    public Requests(Helpee helpee, boolean complete, String requestsJwt,
                    String requestType, String requestDetail,
                    String location, Double x, Double y,
                    Timestamp requestTime) {
        this.helpee = helpee;
        this.complete = complete;
        this.requestsJwt = requestsJwt;
        this.requestType = requestType;
        this.requestDetail = requestDetail;
        this.location = location;
        this.x = x;
        this.y = y;
        this.requestTime = requestTime;
    }

    public void updateRequests(MessageDto messageDto){
        this.complete = true;
    }
}
