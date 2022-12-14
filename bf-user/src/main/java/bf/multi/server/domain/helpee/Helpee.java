package bf.multi.server.domain.helpee;

import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString(exclude = {"user","requestsList"})
@Entity
@Table(name = "HELPEE")
public class Helpee {

    /* User 중에서 Helpee에 해당하는 객체 */
    /* ID, USER_ID, HELP_CNT, AVG_RATE, HEARTS */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    @Column(name = "TYPE", length = 45, nullable = false)
    private String type;

    @Column(name = "INTRO")
    private String intro;

    @Column(name = "REQUEST_CNT")
    private Integer requestCount;

    @Column(name = "AVG_RATE")
    private Double averageRate;

    @Column(name = "HEARTS", nullable = false)
    private Integer hearts;

    @OneToMany(mappedBy = "helpee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Requests> requestsList = new ArrayList<>();

    @Builder
    public Helpee(User user, String type, String intro, Integer requestCount, Double averageRate, Integer hearts) {
        this.user = user;
        this.type = type;
        this.intro = intro;
        this.requestCount = requestCount;
        this.averageRate = averageRate;
        this.hearts = hearts;
    }
}
