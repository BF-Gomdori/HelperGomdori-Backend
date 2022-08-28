package bf.multi.server.domain.dto.helper;

import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HelperSignUpDto {

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public Helper toEntity() {
        return Helper
                .builder()
                .user(user)
                .helpCount(0)
                .averageRate(0.0)
                .hearts(0)
                .build();
    }
}
