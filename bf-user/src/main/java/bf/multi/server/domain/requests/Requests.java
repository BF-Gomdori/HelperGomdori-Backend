package bf.multi.server.domain.requests;

import bf.multi.server.domain.helpee.Helpee;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "REQUESTS")
public class Requests {
    /* Helpee가 요청한 Requests에 관한 정보를 담는 객체 */
    /* ID, HELPEE_ID, MSG, LOCATION, REQUEST_TIME */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "HELPEE_ID", nullable = false, unique = true)
    private Helpee helpee;

    @Column(name = "MSG", columnDefinition = "TEXT")
    private String message;

    @Column(name = "LOCATION", length = 45)
    private String location;

    @Column(name = "REQUEST_TIME", columnDefinition = "TIMESTAMP")
    private Timestamp requestTime;

    /* 연관 관계 편의 메서드 */
    public void changeHelpee() {
        helpee.getRequestsList().add(this);
    }

    @Builder
    public Requests(Helpee helpee, String message, String location, Timestamp requestTime) {
        this.helpee = helpee;
        this.message = message;
        this.location = location;
        this.requestTime = requestTime;
    }
}
