package bf.multi.server.domain.dto.user;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtTokenDto {
    private String token;

    @Builder
    public JwtTokenDto(String token) {
        this.token = token;
    }
}
