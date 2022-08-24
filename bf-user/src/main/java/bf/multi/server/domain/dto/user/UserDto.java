package bf.multi.server.domain.dto.user;

import bf.multi.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {

    private String username;

    private String email;

    public static UserDto from(User user) {
        // Entity to DTO
        return new UserDto((user.getUsername()), user.getEmail());
    }

    @Builder
    public UserDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
