package bf.multi.server.domain.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginDto {

    private String username;

    private String password;

    @Builder
    public UserLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
