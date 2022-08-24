package bf.multi.server.domain.dto.helpee;

import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HelpeeSignUpDto {

    private User user;

    private String type;

    private Integer requestCount;

    private Double averageRate;

    private Integer hearts;

    @Builder
    public HelpeeSignUpDto(String type, Integer requestCount, Double averageRate, Integer hearts) {
        this.type = type;
        this.requestCount = requestCount;
        this.averageRate = averageRate;
        this.hearts = hearts;
    }

    /* User에 대한 Setter */
    public void setUser(User user) {
        this.user = user;
    }

    public Helpee toEntity() {
        return Helpee
                .builder()
                .user(user)
                .type(type)
                .requestCount(requestCount)
                .averageRate(averageRate)
                .hearts(hearts)
                .build();
    }
}
