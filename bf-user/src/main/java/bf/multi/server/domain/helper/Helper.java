package bf.multi.server.domain.helper;

import bf.multi.server.domain.helps.Helps;
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
@ToString(exclude = {"user","helpsList"})
@Entity
@Table(name = "HELPER")
public class Helper {

    /* User 중에서 Helper에 해당하는 객체 */
    /* ID, USER_ID, HELP_CNT, AVG_RATE, HEARTS */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    @Column(name = "HELP_CNT", nullable = false)
    private Integer helpCount;

    @Column(name = "AVG_RATE")
    private Double averageRate;

    @Column(name = "HEARTS", nullable = false)
    private Integer hearts;

    @OneToMany(mappedBy = "helper", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Helps> helpsList = new ArrayList<>();

    @Builder
    public Helper(User user, Integer helpCount, Double averageRate, Integer hearts) {
        this.user = user;
        this.helpCount = helpCount;
        this.averageRate = averageRate;
        this.hearts = hearts;
    }


}
