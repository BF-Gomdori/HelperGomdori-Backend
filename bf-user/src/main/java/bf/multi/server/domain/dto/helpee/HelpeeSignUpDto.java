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

    @Builder
    public HelpeeSignUpDto(String type, Integer requestCount, Double averageRate, Integer hearts) {
        this.type = type;
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
                .requestCount(0)
                .averageRate(0.0)
                .hearts(0)
                .build();
    }
}
