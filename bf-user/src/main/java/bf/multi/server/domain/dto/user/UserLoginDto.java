package bf.multi.server.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginDto {

    private String username;

    private String password;
}
